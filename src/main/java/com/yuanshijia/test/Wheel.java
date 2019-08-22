package com.yuanshijia.test;

import com.yuanshijia.core.Service;

/**
 * @author yuanshijia
 * @date 2019-08-12
 * @description
 * 轮子
 */
@Service(value = "wheel")
public class Wheel {
    public void make() throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("轮子制作中...");
        Thread.sleep(1000);

    }
}
