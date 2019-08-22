package com.yuanshijia.core;

import com.yuanshijia.bean.BeanDefinition;
import com.yuanshijia.bean.BeanFactoryImpl;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author yuanshijia
 * @date 2019-08-12
 * @description
 */
public class AnnotationApplicationContext extends BeanFactoryImpl {
    public void init(){

        Reflections reflections = new Reflections("com.yuanshijia");
        // 获取 @Service 标注的接口
        Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(Service.class);

        List<BeanDefinition> beanDefinitions = new ArrayList<>();
        for (Class<?> c : classSet) {
            BeanDefinition beanDefinition = new BeanDefinition();
            beanDefinition.setClassName(c.getName());

            // 反射获取注解的value属性
            Service service = c.getDeclaredAnnotation(Service.class);
            beanDefinition.setName(service.value());
            beanDefinitions.add(beanDefinition);
        }
        // 注入
        for (BeanDefinition beanDefinition : beanDefinitions) {
            registerBean(beanDefinition.getName(), beanDefinition);
        }
    }
}
