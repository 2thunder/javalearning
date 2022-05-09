package com.posuoren.pattern.behavior.strategy;

/**
 * @Description
 * @Author leiyutian
 * @Date 2020/2/19 17:12
 */
public class LolStrategyImpl implements Strategy {
    /**
     * 娱乐
     */
    @Override
    public void amuse() {
        System.out.println("玩LOL");
    }
}
