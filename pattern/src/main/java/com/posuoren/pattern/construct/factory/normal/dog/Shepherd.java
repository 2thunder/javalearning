package com.posuoren.pattern.construct.factory.normal.dog;

/**
 * @Description
 * @Author leiyutian
 * @Date 2020/2/9 19:57
 */
public class Shepherd implements Dog {
    @Override
    public void play() {
        System.out.println("牧羊犬: 放羊");
    }
}
