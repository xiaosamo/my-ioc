package com.yuanshijia.test;

import com.yuanshijia.core.Service;
import lombok.Data;

/**
 * @author yuanshijia
 * @date 2019-08-12
 * @description
 */
@Service(value = "robot")
public class Robot {
    //需要注入 hand 和 mouth
    private Hand hand;
    private Mouth mouth;

    public void show(){
        hand.waveHand();
        mouth.speak();
    }
}
