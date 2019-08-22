package com.yuanshijia.test;

import com.yuanshijia.core.Service;

/**
 * @author yuanshijia
 * @date 2019-08-12
 * @description
 * 汽车类
 */
@Service(value = "car")
public class Car {
    /**
     * 注入车架和轮子
     */
    private Frame frame;
    private Wheel wheel;


    public void makeCar() throws InterruptedException {
        System.out.println("开始生产汽车...");
        frame.make();
        wheel.make();
        System.out.println("汽车制作完成!");
    }
}
