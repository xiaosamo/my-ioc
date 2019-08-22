package com.yuanshijia.test;

import com.yuanshijia.core.Service;

/**
 * @author yuanshijia
 * @date 2019-08-12
 * @description
 * 车架
 */
@Service(value = "frame")
public class Frame {
    public void make() throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("车架制作中...");
        Thread.sleep(1000);
    }
}
