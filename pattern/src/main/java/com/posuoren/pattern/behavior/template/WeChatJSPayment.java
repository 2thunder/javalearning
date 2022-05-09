package com.posuoren.pattern.behavior.template;

/**
 * @Description
 * @Author leiyutian
 * @Date 2020/2/11 11:13
 */
public class WeChatJSPayment extends AbstractPayment {
    @Override
    protected void generateSingInfo() {
        System.out.println("WechatJSPayment 生成签名信息");
    }

    @Override
    protected void createPlatformOrder() {
        System.out.println("WechatJSPayment 生成平台订单");
    }
}
