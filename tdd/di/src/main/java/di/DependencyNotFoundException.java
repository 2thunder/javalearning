package di;

public class DependencyNotFoundException extends RuntimeException {
    /**
     * 依赖类型
     */
    private final Class<?> dependencyClass;

    /**
     * 组件类型
     */
    private Class<?> componentClass;

    public DependencyNotFoundException(Class<?> dependencyClass) {
        this.dependencyClass = dependencyClass;
    }

    public DependencyNotFoundException(Class<?> dependencyClass, Class<?> componentClass) {
        this.dependencyClass = dependencyClass;
        this.componentClass = componentClass;
    }

    public Class<?> getDependency() {
        return dependencyClass;
    }

    public Class<?> getComponent() {
        return componentClass;
    }
}
