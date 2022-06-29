package di;

import jakarta.inject.Inject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;


/**
 * 容器
 *
 * @author 2thunder
 * @version V1.0
 * @date 2022/5/31 09:34
 */
public class ContextConfig {

    private final Map<Class<?>, ComponentProvider<?>> providers = new HashMap<>();

    private final Map<Class<?>, List<Class<?>>> dependencies = new HashMap<>();

    public <ComponentType> void bind(Class<ComponentType> type, ComponentType instance) {
        providers.put(type, context -> instance);
        dependencies.put(type, new ArrayList<>());
    }

    public <Type, Implementation> void bind(Class<Type> componentClass, Class<Implementation> implementation) {
        Constructor<Implementation> constructor = getInjectConstructor(implementation);
        providers.put(componentClass, new ConstructorInjectProvider(constructor, componentClass));
        dependencies.put(componentClass, stream(constructor.getParameters()).map(Parameter::getType).collect(Collectors.toList()));
    }

    public Context getContext() {
        dependencies.keySet().forEach(component -> checkDependencies(component, new Stack<>()));
        return new Context() {
            @Override
            public <T> Optional<T> get(Class<T> componentClass) {
                return Optional.ofNullable(providers.get(componentClass)).map(provider -> (T) provider.get(this));
            }
        };
    }

    private void checkDependencies(Class<?> component, Stack<Class<?>> visiting) {
        for (Class<?> dependency : dependencies.get(component)) {
            if (!dependencies.containsKey(dependency)) {
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
    }

    public class ConstructorInjectProvider<T> implements ComponentProvider<T> {

        private final Constructor<T> constructor;

        private final Class<?> componentType;

        public ConstructorInjectProvider(Constructor<T> constructor, Class<?> componentType) {
            this.constructor = constructor;
            this.componentType = componentType;
        }

        @Override
        public T get(Context context) {
            try {
                // get dependency instance
                Object[] dependencies = stream(constructor.getParameters())
                    .map(p -> context.get(p.getType()).get())
                    .toArray();
                return constructor.newInstance(dependencies);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private <T> Constructor<T> getInjectConstructor(Class<T> implementation) {
        List<Constructor<?>> injectConstructors = stream(implementation.getConstructors())
            .filter(c -> c.isAnnotationPresent(Inject.class)).collect(Collectors.toList());
        if (injectConstructors.size() > 1) {
            throw new IllegalComponentException();
        }
        return (Constructor<T>) injectConstructors.stream().findFirst().orElseGet(() -> {
            try {
                return implementation.getConstructor();
            } catch (NoSuchMethodException e) {
                throw new IllegalComponentException();
            }
        });
    }
}
