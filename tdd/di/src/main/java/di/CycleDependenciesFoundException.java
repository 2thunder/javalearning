package di;

import java.util.HashSet;
import java.util.Set;

/**
 * 循环依赖异常
 *
 * @author leiyutian yutianlei@shein.com
 * @version V1.0
 * @date 2022/5/17 13:36
 */
public class CycleDependenciesFoundException extends RuntimeException {
    private final Set<Class<?>> components = new HashSet<>();

    public CycleDependenciesFoundException(Class<?> type) {
        components.add(type);
    }

    public CycleDependenciesFoundException(Class<?> componentType, CycleDependenciesFoundException e) {
        components.add(componentType);
        components.addAll(e.getComponents());
    }

    public Set<Class<?>> getComponents() {
        return components;
    }
}
