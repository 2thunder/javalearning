package di;

import jakarta.inject.Inject;
import jakarta.inject.Provider;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Context {
    private Map<Class<?>, Provider<?>> providers = new HashMap<>();

    public <ComponentType> void bind(Class<ComponentType> type, ComponentType instance) {
        providers.put(type, () -> instance);
    }

    public <Type, Implementation> void bind(Class<Type> componentClass, Class<Implementation> implementation) {
        providers.put(componentClass, (Provider<Type>) () -> {
            try {
                Constructor<Implementation> constructor = getInjectConstructor(implementation);
                // get dependency instance
                Object[] dependencies = Arrays.stream(constructor.getParameters())
                    .map(p -> get(p.getType()))
                    .toArray();
                return (Type) constructor.newInstance(dependencies);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }


    private <T> Constructor<T> getInjectConstructor(Class<T> implementation){
        Stream<Constructor<?>> injectConstructors = Arrays.stream(implementation.getConstructors())
            .filter(c -> c.isAnnotationPresent(Inject.class));

        return (Constructor<T>) injectConstructors
            .findFirst()
            .orElseGet(() -> getDefaultConstructor(implementation));
    }

    private <T> Constructor<T> getDefaultConstructor(Class<T> implementation) {
        try {
            return implementation.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public <Type> Type get(Class<Type> type) {
        return (Type) providers.get(type).get();
    }

}
