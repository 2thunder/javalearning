package com.posuoren.pattern.behavior.strategy;

/**
 * @Description
 * @Author leiyutian
 * @Date 2020/2/19 17:13
 */
public class MovieStrategyImpl implements Strategy {
    /**
     * 娱乐
     */
    @Override
    public void amuse() {
        System.out.println("看电影");
    }
}
