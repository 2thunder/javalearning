package com.posuoren.pattern.behavior.responsibility;

/**
 * @Description
 * @Author leiyutian
 * @Date 2020/2/19 17:27
 */
public class StringEndFilter extends AbstractFilter {
    public StringEndFilter(AbstractFilter nextFilter) {
        super(nextFilter);
    }

    public StringEndFilter() {
        super();
    }

    /**
     * 筛选
     */
    @Override
    String filter(String message) {
        return message + ":end";
    }
}
