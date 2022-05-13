package di;

import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ContainerTest {

    Context context;

    @BeforeEach
    public void setUp() {
        context = new Context();
    }


    @Nested
    public class ComponentConstruction {
        @Nested
        public class DependenciesSelection {
            // TODO: instance
            @Test
            public void should_bind_type_to_a_specific_instance() {
                Component instance = new Component() {
                };
                context.bind(Component.class, instance);
                assertSame(instance, context.get(Component.class).get());
            }

            // TODO: abstract class
            // TODO: interface

            @Test
            public void should(){

                Optional<Component> componentOptional = context.get(Component.class);
                assertTrue(componentOptional.isEmpty());
            }

            @Nested
            public class ConstructorInjection {
                // TODO: No args constructor
                @Test
                public void should_bind_type_to_a_class_with_default_constructor() {
                    context.bind(Component.class, ComponentWithDefaultConstructorImplementation.class);

                    Component instance = context.get(Component.class).get();
                    assertNotNull(instance);
                    assertTrue(instance instanceof ComponentWithDefaultConstructorImplementation);
                }

                // TODO: with dependency
                @Test
                public void should_bind_type_to_a_class_with_inject_constructor() {

                    Dependency dependency = new Dependency() {
                    };

                    context.bind(Component.class, ComponentWithInjectConstructorImplementation.class);
                    context.bind(Dependency.class, dependency);

                    Component instance = context.get(Component.class).get();
                    assertNotNull(instance);
                    assertSame(dependency, ((ComponentWithInjectConstructorImplementation) instance).getDependency());
                }

                // TODO: A -> B -> C
                @Test
                public void should_bind_type_to_a_class_with_transitive_dependencies() {
                    context.bind(Component.class, ComponentWithInjectConstructorImplementation.class);
                    context.bind(Dependency.class, DependencyWithInjectConstructor.class);
                    context.bind(String.class, "indirect dependency");

                    Component instance = context.get(Component.class).get();
                    assertNotNull(instance);

                    Dependency dependency = ((ComponentWithInjectConstructorImplementation) instance).getDependency();
                    assertNotNull(dependency);

                    assertEquals("indirect dependency", ((DependencyWithInjectConstructor) dependency).getDependency());
                }

                @Test
                public void should_throw_exception_if_multi_inject_constructors_provided() {
                    assertThrows(
                        IllegalComponentException.class,
                        () -> context.bind(Component.class, ComponentWithMultiInjectConstructorImplementation.class)
                    );
                }

                @Test
                public void should_throw_exception_if_no_inject_nodefault() {
                    assertThrows(
                        IllegalComponentException.class,
                        () -> context.bind(Component.class, ComponentWithMultiInjectConstructorImplementation.class)
                    );

                }

                // no dependency
                @Test
                public void should_throw_exception_if_dependency_not_found() {
                    context.bind(Component.class, ComponentWithInjectConstructorImplementation.class);
                    assertThrows(
                        DependencyNotFoundException.class,
                        () -> context.get(Component.class)
                    );
                }
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

interface Component {

}

interface Dependency {

}

class ComponentWithDefaultConstructorImplementation implements Component {
    public ComponentWithDefaultConstructorImplementation() {
    }
}

class ComponentWithInjectConstructorImplementation implements Component {

    private Dependency dependency;

    @Inject
    public ComponentWithInjectConstructorImplementation(Dependency dependency) {
        this.dependency = dependency;
    }

    public Dependency getDependency() {
        return dependency;
    }
}

class ComponentWithMultiInjectConstructorImplementation implements Component {

    private String dependency;

    private Double test;

    @Inject
    public ComponentWithMultiInjectConstructorImplementation(String dependency) {
        this.dependency = dependency;
    }

    @Inject
    public ComponentWithMultiInjectConstructorImplementation(String dependency, Double test) {
        this.dependency = dependency;
        this.test = test;
    }
}

class DependencyWithInjectConstructor implements Dependency {
    String dependency;

    @Inject
    public DependencyWithInjectConstructor(String dependency) {
        this.dependency = dependency;
    }

    public String getDependency() {
        return dependency;
    }
}

class ComponentWithNoInjectConstructor implements Component {
    String dependency;

    public ComponentWithNoInjectConstructor(String dependency) {
        this.dependency = dependency;
    }

    public String getDependency() {
        return dependency;
    }
}