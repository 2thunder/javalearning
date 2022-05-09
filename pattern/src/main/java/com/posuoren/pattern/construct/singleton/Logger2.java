package com.posuoren.pattern.construct.singleton;

/**
 * @Description 支持懒加载，且线程安全， 交给JVM初始化
 * @Author leiyutian
 * @Date 2020/2/9 19:21
 */
public class Logger2 {
    private Logger2() {

    }

    private static class Logger2Holder{
        private static final Logger2 instance = new Logger2();
    }

    public static Logger2 getInstance() {
        return Logger2Holder.instance;
    }
}
