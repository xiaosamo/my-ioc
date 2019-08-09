package com.yuanshijia.bean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yuanshijia
 * @date 2019-08-09
 * @description
 */
public class BeanFactoryImpl implements BeanFactory {

    private static final Map<String, Object> beanMap = new ConcurrentHashMap<>();

    @Override
    public Object getBean(String name) throws Exception {
        Object bean = beanMap.get(name);
        if (bean != null) {
            return bean;
        }

        bean = createBean();
        if (bean != null) {

            // 把对象存入Map中方便下次使用
            beanMap.put(bean);
        }
        return bean;
    }

}
