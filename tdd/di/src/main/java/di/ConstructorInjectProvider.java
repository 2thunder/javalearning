package di;

import jakarta.inject.Inject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

public class ConstructorInjectProvider<T> implements ContextConfig.ComponentProvider<T> {

    private final Constructor<T> injectConstructor;

    private List<Field> injectFields;

    public ConstructorInjectProvider(Class<T> component) {
        this.injectConstructor = getInjectConstructor(component);
        this.injectFields = getInjectFields(component);
    }


    @Override
    public T get(Context context) {
        try {
            // get dependency instance
            Object[] dependencies = stream(injectConstructor.getParameters())
                .map(p -> context.get(p.getType()).get())
                .toArray();
            T instance = injectConstructor.newInstance(dependencies);
            for (Field field : injectFields) {
                field.set(instance, context.get(field.getType()).get());
            }
            return instance;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Class<?>> getDependencies() {
        return Stream.concat(stream(injectConstructor.getParameters()).map(Parameter::getType), injectFields.stream().map(Field::getType))
            .toList();
    }


    private static <T> List<Field> getInjectFields(Class<T> component) {
        List<Field> injectFields = new ArrayList<>();
        Class<?> current = component;
        while (current != Object.class) {
            injectFields.addAll(stream(current.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Inject.class))
                .toList());
            current = current.getSuperclass();
        }
        return injectFields;
    }

    private static <T> Constructor<T> getInjectConstructor(Class<T> implementation) {
        List<Constructor<?>> injectConstructors = stream(implementation.getConstructors())
            .filter(c -> c.isAnnotationPresent(Inject.class)).collect(Collectors.toList());
        if (injectConstructors.size() > 1) {
            throw new IllegalComponentException();
        }
        return (Constructor<T>) injectConstructors.stream().findFirst().orElseGet(() -> {
            try {
                return implementation.getDeclaredConstructor();
            } catch (NoSuchMethodException e) {
                throw new IllegalComponentException();
            }
        });
    }
}
