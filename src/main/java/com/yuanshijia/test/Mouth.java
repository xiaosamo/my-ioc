package com.yuanshijia.test;

import com.yuanshijia.core.Service;

/**
 * @author yuanshijia
 * @date 2019-08-12
 * @description
 */
@Service(value = "mouth")
public class Mouth {
    public void speak(){
        System.out.println("say hello world");
    }
}
