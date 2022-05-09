package io.thunder.tdd.di;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ContainerTest {

    interface Component {

    }

    static class ComponentImplementation implements Component {
        public ComponentImplementation() {
        }
    }

    @Nested
    public class ComponentConstruction {
        @Nested
        public class DependenciesSelection {
            // TODO: instance
            @Test
            public void should_bind_type_to_a_specific_instance() {
                Context context = new Context();
                Component instance = new Component() {
                };
                context.bind(Component.class, instance);
                assertSame(instance, context.get(Component.class));
            }

            // TODO: abstract class
            // TODO: interface

            @Nested
            public class ConstructorInjection {
                // TODO: No args constructor
                @Test
                public void should_bind_type_to_a_class_with_default_constructor() {
                    Context context = new Context();
                    context.bind(Component.class, ComponentImplementation.class);

                    Component instance = context.get(Component.class);
                    assertNotNull(instance);
                    assertTrue(instance instanceof ComponentImplementation);
                }
                // TODO: with dependency
                // TODO: A -> B -> C
            }

            @Nested
            public class FiledInjection {

            }

            @Nested
            public class MethodInjection {

            }

        }


    }

    @Nested
    public class LifecycleManagement {

    }
}