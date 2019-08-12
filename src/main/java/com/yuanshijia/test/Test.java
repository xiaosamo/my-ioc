package com.yuanshijia.test;

import com.yuanshijia.core.AnnotationApplcationContext;

/**
 * @author yuanshijia
 * @date 2019-08-12
 * @description
 */
public class Test {
    public static void main(String[] args) throws Exception {
        AnnotationApplcationContext context = new AnnotationApplcationContext();
        context.init();
        Robot robot = (Robot) context.getBean(Robot.class.getName());

        Hand hand = (Hand) context.getBean(Hand.class.getName());
        Mouth mouth = (Mouth) context.getBean(Mouth.class.getName());

        System.out.println(hand);
        System.out.println(mouth);
        robot.show();
    }
}
