package di;

import jakarta.inject.Inject;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

public class ConstructorInjectProvider<T> implements ContextConfig.ComponentProvider<T> {

    private final Constructor<T> injectConstructor;

    private List<Field> injectFields;

    private final List<Method> injectMethods;

    public ConstructorInjectProvider(Class<T> component) {
        this.injectConstructor = getInjectConstructor(component);
        this.injectFields = getInjectFields(component);
        this.injectMethods = getInjectMethods(component);
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
            for (Method method : injectMethods) {
                method.invoke(instance, stream(method.getParameterTypes()).map(t -> context.get(t).get()).toArray(Object[]::new));
            }
            return instance;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Class<?>> getDependencies() {
        return Stream.concat(Stream.concat(stream(injectConstructor.getParameters()).map(Parameter::getType), injectFields.stream().map(Field::getType)),
                injectMethods.stream().flatMap(m -> stream(m.getParameterTypes())))
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

    private static <T> List<Method> getInjectMethods(Class<T> component) {
        List<Method> result = new ArrayList<>();
        Class<?> current = component;
        while (current != Object.class) {
            result.addAll(stream(current.getDeclaredMethods()).filter(m -> m.isAnnotationPresent(Inject.class)).toList());
            current = current.getSuperclass();
        }

        Collections.reverse(result);
        return result;
    }
}
