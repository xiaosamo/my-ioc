package com.yuanshijia.bean;

import com.yuanshijia.utils.BeanUtil;
import com.yuanshijia.utils.ClassUtil;
import com.yuanshijia.utils.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yuanshijia
 * @date 2019-08-09
 * @description
 */
public class BeanFactoryImpl implements BeanFactory {

    private static final Map<String, Object> beanMap = new ConcurrentHashMap<>();


    private static final Map<String, BeanDefinition> beanDefineMap = new ConcurrentHashMap<>();


    private static final Set<String> beanNameSet = Collections.synchronizedSet(new HashSet<>());

    /**
     * 1.当调用 getBean() 的方法的时候。会先到 beanMap 里面查找，有没有实例化好的对象。
     * 2.如果没有，就会去beanDefineMap查找这个对象对应的 BeanDefination。再利用DeanDefination去实例化一个对象。
     * @param name
     * @return
     * @throws Exception
     */
    @Override
    public Object getBean(String name) throws Exception {
        Object bean = beanMap.get(name);
        if (bean != null) {
            return bean;
        }

        // 没有实例化好的对象,进行第2步
        bean = createBean(beanDefineMap.get(name));

        if (bean != null) {

            //对象创建成功以后，注入对象需要的参数
            populatebean(bean);

            // 把对象存入Map中方便下次使用
            beanMap.put(name, bean);
        }
        return bean;
    }

    private Object createBean(BeanDefinition beanDefinition) throws Exception {

        String className = beanDefinition.getClassName();
        Class clazz = ClassUtil.loadClass(className);
        if(clazz == null) {
            throw new Exception("can not find bean by beanName");
        }
        List<ConstructorArg> constructorArgs = beanDefinition.getConstructorArgs();
        if (constructorArgs != null && !constructorArgs.isEmpty()) {
            List<Object> objects = new ArrayList<>();
            for (ConstructorArg constructorArg : constructorArgs) {
                objects.add(getBean(constructorArg.getRef()));
            }
            return BeanUtil.instanceByCglib(clazz, clazz.getConstructor(), (Objects[])  objects.toArray());
        } else {
            return BeanUtil.instanceByCglib(clazz, null, null);
        }
    }


    protected void registerBean(String name, BeanDefinition beanDefinition) {
        beanDefineMap.put(name, beanDefinition);
        beanNameSet.add(name);
    }

    /**
     * 对象实例化成功以后，我们还需要注入相应的参数，调用 populatebean()这个方法。在 populateBean 这个方法中，会扫描对象里面的Field，如果对象中的 Field 是我们IoC容器管理的对象，那就会调用 我们上文实现的 ReflectionUtils.injectField来注入对象。
     * @param bean
     *
     */
    private void populatebean(Object bean) throws Exception {
        Field[] fields = bean.getClass().getSuperclass().getDeclaredFields();
        if (fields.length > 0) {
            for (Field field : fields) {
                String className = field.getType().getName();
                if (beanNameSet.contains(className)) {
                    Object fieldBean = getBean(className);
                    if (fieldBean != null) {
                        ReflectionUtil.injectField(field, bean, fieldBean);
                    }
                }
            }
        }

    }

}
