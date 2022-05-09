package com.posuoren.pattern.behavior.responsibility;

/**
 * @Description
 * @Author leiyutian
 * @Date 2020/2/19 17:27
 */
public class StringStartFilter extends AbstractFilter {
    public StringStartFilter(AbstractFilter nextFilter) {
        super(nextFilter);
    }

    public StringStartFilter(){
        super();
    }

    /**
     * 筛选
     */
    @Override
    public String filter(String message) {
        return "start: " + message;
    }
}
