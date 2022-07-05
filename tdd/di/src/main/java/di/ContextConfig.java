package di;

import java.util.*;


/**
 * 容器
 *
 * @author 2thunder
 * @version V1.0
 * @date 2022/5/31 09:34
 */
public class ContextConfig {

    private final Map<Class<?>, ComponentProvider<?>> providers = new HashMap<>();

    public <ComponentType> void bind(Class<ComponentType> type, ComponentType instance) {
        providers.put(type, new ComponentProvider<ComponentType>() {
            @Override
            public ComponentType get(Context context) {
                return instance;
            }

            @Override
            public List<Class<?>> getDependencies() {
                return List.of();
            }
        });
    }

    public <Type, Implementation> void bind(Class<Type> componentClass, Class<Implementation> implementation) {
        providers.put(componentClass, new ConstructorInjectProvider(implementation));
    }

    public Context getContext() {
        providers.keySet().forEach(component -> checkDependencies(component, new Stack<>()));
        return new Context() {
            @Override
            public <T> Optional<T> get(Class<T> componentClass) {
                return Optional.ofNullable(providers.get(componentClass)).map(provider -> (T) provider.get(this));
            }
        };
    }

    private void checkDependencies(Class<?> component, Stack<Class<?>> visiting) {
        for (Class<?> dependency : providers.get(component).getDependencies()) {
            if (!providers.containsKey(dependency)) {
                throw new DependencyNotFoundException(dependency, component);
            }
            if (visiting.contains(dependency)) {
                throw new CycleDependenciesFoundException(visiting);
            }
            visiting.push(dependency);
            checkDependencies(dependency, visiting);
            visiting.pop();
        }
    }

    interface ComponentProvider<T> {
        T get(Context context);

        List<Class<?>> getDependencies();
    }

}
