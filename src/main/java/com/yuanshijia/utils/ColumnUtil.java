package com.yuanshijia.utils;

import com.yuanshijia.core.Service;
import com.yuanshijia.test.Hand;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @author yuanshijia
 * @date 2019-08-12
 * @description
 */
public class ColumnUtil {
    public static void main(String[] args) {
        String[] str=getColumnValue(Hand.class);
        for (String s : str) {
            System.out.println(s);
        }
    }
    public static String[] getColumnValue(Class classz) {
        Field[] fields=classz.getDeclaredFields();


        Annotation[] annotationsByType = classz.getAnnotationsByType(Service.class);
        for (int i = 0; i < annotationsByType.length; i++) {

            System.out.println(annotationsByType[i]);
        }


            Field field;
        String[] value=new String[fields.length];
        for (int i = 0; i <fields.length ; i++) {
            fields[i].setAccessible(true);
        }
        for (int i = 0; i < fields.length; i++) {
            
            try {
                field = classz.getDeclaredField(fields[i].getName());
                Service column = field.getAnnotation(Service.class); //获取指定类型注解
                if (column != null) {
                    value[i] = column.value();
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return value;
    }
}
