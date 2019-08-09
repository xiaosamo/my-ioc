package com.yuanshijia.utils;

/**
 * @author yuanshijia
 * @date 2019-08-09
 * @description
 */
public class ClassUtil {

    public static ClassLoader getDefultClassLoader(){
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 通过 className 参数获取对象的 Class
     * @param className
     * @return
     */
    public static Class loadClass(String className) {
        try {
            return getDefultClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
