package com.posuoren.pattern.construct.factory.abstractfactory.cat;

/**
 * @Description 美国短毛猫
 * @Author leiyutian
 * @Date 2020/2/11 10:17
 */
public class AmericanShortHair implements Cat {
    /**
     * 说爱好
     */
    @Override
    public void sayHobby() {
        System.out.println("美国短毛猫: 被主人撸猫");
    }
}
