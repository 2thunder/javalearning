package com.posuoren.pattern.construct.factory.abstractfactory.cat;

/**
 * @Description
 * @Author leiyutian
 * @Date 2020/2/11 10:22
 */
public class BritishShortHair implements Cat {

    /**
     * 说爱好
     */
    @Override
    public void sayHobby() {
        System.out.println("英国短毛猫: 躺着");
    }
}
