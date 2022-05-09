package com.posuoren.pattern.construct.factory.abstractfactory.cat;

/**
 * @Description
 * @Author leiyutian
 * @Date 2020/2/11 10:28
 */
public class CatFactory {

    public Cat getCat(String name){
        if(name == null){
            return null;
        }
        if(name.equalsIgnoreCase("AmericanShortHair")){
            return new AmericanShortHair();
        }else if(name.equalsIgnoreCase("BritishShortHair")){
            return new BritishShortHair();
        }
        return null;
    }
}
