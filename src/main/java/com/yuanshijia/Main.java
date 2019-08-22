package com.yuanshijia;

import com.yuanshijia.core.AnnotationApplicationContext;
import com.yuanshijia.test.Car;

/**
 * @author yuanshijia
 * @date 2019-08-12
 * @description
 */
public class Main {
    public static void main(String[] args) throws Exception {
        AnnotationApplicationContext context = new AnnotationApplicationContext();
        context.init();
        Car car = (Car) context.getBean("car");

        car.makeCar();
    }
}
