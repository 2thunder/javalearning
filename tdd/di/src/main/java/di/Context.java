package di;

import jakarta.inject.Inject;
import jakarta.inject.Provider;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 容器
 *
 * @author 2thunder
 * @version V1.0
 * @date 2022/5/31 09:34
 */
public class Context {
    private Map<Class<?>, Provider<?>> providers = new HashMap<>();

    public <ComponentType> void bind(Class<ComponentType> type, ComponentType instance) {
        providers.put(type, () -> instance);
    }

    public <Type, Implementation> void bind(Class<Type> componentClass, Class<Implementation> implementation) {
        Constructor<Implementation> constructor = getInjectConstructor(implementation);
        providers.put(componentClass, new ConstructorInjectProvider(constructor, componentClass));
    }

    public class ConstructorInjectProvider<T> implements Provider<T> {

        private final Constructor<T> constructor;

        private final Class<?> componentType;

        private boolean constructing = false;


        public ConstructorInjectProvider(Constructor<T> constructor, Class<?> componentType) {
            this.constructor = constructor;
            this.componentType = componentType;
        }

        @Override
        public T get() {
            if (constructing) {
                throw new CycleDependenciesFoundException();
            }
            try {
                constructing = true;
                // get dependency instance
                Object[] dependencies = Arrays.stream(constructor.getParameters())
                    .map(p -> Context.this.get(p.getType()).orElseThrow(() -> new DependencyNotFoundException(p.getType(), componentType)))
                    .toArray();
                return constructor.newInstance(dependencies);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            } finally {
                constructing = false;
            }
        }
    }


    private <T> Constructor<T> getInjectConstructor(Class<T> implementation) {
        List<Constructor<?>> injectConstructors = Arrays.stream(implementation.getConstructors())
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

    public <T> Optional<T> get(Class<T> componentClass) {
        return Optional.ofNullable(providers.get(componentClass)).map(provider -> (T) provider.get());
    }
}
