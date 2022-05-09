package io.thunder.tdd.di;

import jakarta.inject.Provider;

import java.util.HashMap;
import java.util.Map;

public class Context {
    private Map<Class<?>, Provider<?>> providers = new HashMap<>();

    public <ComponentType> void bind(Class<ComponentType> type, ComponentType instance) {
        providers.put(type, () -> instance);
    }

    public <T, I> void bind(Class<T> componentClass, Class<I> implementationClass) {
        providers.put(componentClass, (Provider<T>) () -> {
            try {
                return componentClass.cast(implementationClass.getConstructor().newInstance());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public <T> T get(Class<T> componentClass) {
        return componentClass.cast(providers.get(componentClass).get());
    }

}
