package com.posuoren.pattern.behavior.strategy;

/**
 * @Description
 * @Author leiyutian
 * @Date 2020/2/19 17:13
 */
public class AmuseContext {
    private Strategy strategy;

    public AmuseContext(Strategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public void amuse(){
        strategy.amuse();
    }
}
