package com.yuanshijia.utils;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import java.lang.reflect.Constructor;
import java.util.Objects;

/**
 * @author yuanshijia
 * @date 2019-08-09
 * @description
 * 负责处理对象的实例化，这里我们使用了 cglib 这个工具包
 */
public class BeanUtil {
    public static <T> T instanceByCglib(Class<T> clazz, Constructor ctr, Objects[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(NoOp.INSTANCE);
        if (ctr == null) {
            return (T) enhancer.create();
        } else {
            return (T) enhancer.create(ctr.getParameterTypes(), args);
        }
    }
}
