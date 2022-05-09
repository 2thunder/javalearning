package com.posuoren.pattern.behavior.template;

/**
 * @Description
 * @Author leiyutian
 * @Date 2020/2/11 11:06
 */
public abstract class AbstractPayment {
    public void createOrder(){
        createPlatformOrder();
        generateSingInfo();
    }

    abstract protected void generateSingInfo();

    abstract protected void createPlatformOrder();
}
