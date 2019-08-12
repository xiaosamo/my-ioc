package com.yuanshijia.core;

import com.yuanshijia.bean.BeanDefinition;
import com.yuanshijia.bean.BeanFactoryImpl;
import com.yuanshijia.test.Robot;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author yuanshijia
 * @date 2019-08-12
 * @description
 */
public class AnnotationApplcationContext extends BeanFactoryImpl {
    public void init(){

        Reflections reflections = new Reflections("com.yuanshijia");
        // 获取 @Service 标注的接口
        Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(Service.class);

        List<BeanDefinition> beanDefinitions = new ArrayList<>();
        for (Class<?> c : classSet) {
            BeanDefinition beanDefinition = new BeanDefinition();
            beanDefinition.setClassName(c.getName());

            // todo 反射获取注解的属性
            beanDefinition.setName(c.getName());
            beanDefinitions.add(beanDefinition);
        }

        for (BeanDefinition beanDefinition : beanDefinitions) {
            registerBean(beanDefinition.getName(), beanDefinition);
        }
    }
}
