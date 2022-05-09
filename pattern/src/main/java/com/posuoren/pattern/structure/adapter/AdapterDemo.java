package com.posuoren.pattern.structure.adapter;

/**
 * @Description
 * @Author leiyutian
 * @Date 2020/2/11 17:02
 */
public class AdapterDemo {
    public static void main(String[] args) {
        /*
         * 使用适配器的客户端
         */
        //创建需要被适配的对象
        TypecInterface typecInterface = new TypecInterface();
        //创建客户端需要调用的接口对象
        TypecAdapter typecAdapter = new TypecAdapter(typecInterface);
        //请求处理
        typecAdapter.linkToDataSource();
    }
}
