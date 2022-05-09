package com.posuoren.pattern.structure.adapter;

/**
 * @Description
 * @Author leiyutian
 * @Date 2020/2/11 16:59
 */
public class TypecAdapter implements DisplayLinkInterface {
    private TypecInterface typecInterface;

    public TypecAdapter(TypecInterface typecInterface){
        this.typecInterface = typecInterface;
    }


    /**
     * 连接到数据源
     */
    @Override
    public void linkToDataSource() {
        this.typecInterface.link();
    }
}
