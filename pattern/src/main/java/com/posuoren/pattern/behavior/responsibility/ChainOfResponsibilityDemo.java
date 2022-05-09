package com.posuoren.pattern.behavior.responsibility;

/**
 * @Description
 * @Author leiyutian
 * @Date 2020/2/19 17:36
 */
public class ChainOfResponsibilityDemo {

    public static void main(String[] args) {
        StringStartFilter stringStartFilter = new StringStartFilter(new StringEndFilter());
        System.out.println(stringStartFilter.doFilter("test"));
    }
}
