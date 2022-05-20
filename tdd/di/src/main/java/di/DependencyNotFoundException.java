package di;

public class DependencyNotFoundException extends RuntimeException {
    Class<?> denpendencyClass;

    public DependencyNotFoundException(Class<?> denpendencyClass) {
        this.denpendencyClass = denpendencyClass;
    }

    public Class<?> getDenpendency() {
        return denpendencyClass;
    }
}
