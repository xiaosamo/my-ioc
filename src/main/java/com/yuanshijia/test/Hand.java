package com.yuanshijia.test;

import com.yuanshijia.core.Service;

/**
 * @author yuanshijia
 * @date 2019-08-12
 * @description
 */
@Service(value = "hand")
public class Hand {
    public void waveHand(){
        System.out.println("挥一挥手");
    }
}
