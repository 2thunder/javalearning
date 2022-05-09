package com.posuoren.pattern.behavior.template;

/**
 * @Description
 * @Author leiyutian
 * @Date 2020/2/11 11:14
 */
public class AliPayJsPayment extends AbstractPayment {
    @Override
    protected void generateSingInfo() {
        System.out.println("AliPayJsPayment 生成签名信息");
    }

    @Override
    protected void createPlatformOrder() {
        System.out.println("AliPayJsPayment 生成平台订单");
    }
}
