package di;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * 循环依赖异常
 *
 * @version V1.0
 * @date 2022/5/17 13:36
 */
public class CycleDependenciesFoundException extends RuntimeException {
    private final Set<Class<?>> components = new HashSet<>();

    public CycleDependenciesFoundException(Stack<Class<?>> visiting) {
        components.addAll(visiting);
    }

    public Set<Class<?>> getComponents() {
        return components;
    }
}
