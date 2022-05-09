package com.posuoren.pattern.behavior.template;

/**
 * @Description
 * @Author leiyutian
 * @Date 2020/2/11 11:16
 */
public class PaymentDemo {
    public static void main(String[] args){
        AbstractPayment payment = new AliPayJsPayment();
        payment.createOrder();

        AbstractPayment payment2 = new WeChatJSPayment();
        payment2.createOrder();
    }
}
