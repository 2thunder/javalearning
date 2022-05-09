package com.posuoren.pattern.behavior.responsibility;

/**
 * @Description
 * @Author leiyutian
 * @Date 2020/2/19 17:23
 */
public abstract class AbstractFilter {
    private AbstractFilter nextFilter;

    public AbstractFilter(AbstractFilter nextFilter) {
        this.nextFilter = nextFilter;
    }

    public AbstractFilter() {
    }

    public AbstractFilter getNextFilter() {
        return nextFilter;
    }

    public void setNextFilter(AbstractFilter nextFilter) {
        this.nextFilter = nextFilter;
    }

    public String doFilter(String message){
        String filteredMessage = filter(message);
        if(nextFilter != null){
            filteredMessage = nextFilter.doFilter(filteredMessage);
        }
        return filteredMessage;
    }

    /**
     * 筛选
     */
    abstract String filter(String message);
}
