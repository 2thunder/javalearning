package com.posuoren.pattern.construct.singleton;

/**
 * @Description 懒加载、线程安全
 * @Author leiyutian
 * @Date 2020/2/9 19:21
 */
public class Logger1 {
    private static Logger1 instance;

    private Logger1() {

    }

    public static Logger1 getInstance() {
        if (instance == null) {
            synchronized (Logger1.class) {
                // 类级别锁
                if (instance == null) {
                    instance = new Logger1();
                }
            }
        }
        return instance;
    }
}
