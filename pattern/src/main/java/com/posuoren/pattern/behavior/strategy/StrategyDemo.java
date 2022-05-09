package com.posuoren.pattern.behavior.strategy;

/**
 * @Description
 * @Author leiyutian
 * @Date 2020/2/19 17:14
 */
public class StrategyDemo {

    public static void main(String[] args) {
        Strategy strategy = new LolStrategyImpl();
        AmuseContext amuseContext = new AmuseContext(strategy);
        amuseContext.amuse();

        amuseContext.setStrategy(new MovieStrategyImpl());
        amuseContext.amuse();
    }
}
