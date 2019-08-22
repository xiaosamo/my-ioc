package com.yuanshijia.utils;

import java.lang.reflect.Field;

/**
 * @author yuanshijia
 * @date 2019-08-12
 * @description
 * 通过 Java 的反射原理来完成对象的依赖注入：
 */
public class ReflectionUtil {
    public static void injectField(Field field, Object obj, Object value) throws IllegalAccessException {
        if (field != null) {
            field.setAccessible(true);
            field.set(obj, value);
        }
    }

}
