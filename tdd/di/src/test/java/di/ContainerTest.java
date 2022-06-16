package di;

import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ContainerTest {

    ContextConfig contextConfig;

    @BeforeEach
    public void setUp() {
        contextConfig = new ContextConfig();
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
                contextConfig.bind(Component.class, instance);
                assertSame(instance, contextConfig.getContext().get(Component.class).get());
            }

            // TODO: abstract class
            // TODO: interface

            @Test
            void should_return_optional_if_component_not_defined() {
                Optional<Component> componentOptional = contextConfig.getContext().get(Component.class);
                assertTrue(componentOptional.isEmpty());
            }

            @Nested
            public class ConstructorInjection {
                // TODO: No args constructor
                @Test
                public void should_bind_type_to_a_class_with_default_constructor() {
                    contextConfig.bind(Component.class, ComponentWithDefaultConstructorImplementation.class);

                    Component instance = contextConfig.getContext().get(Component.class).get();
                    assertNotNull(instance);
                    assertTrue(instance instanceof ComponentWithDefaultConstructorImplementation);
                }

                // TODO: with dependency
                @Test
                public void should_bind_type_to_a_class_with_inject_constructor() {

                    Dependency dependency = new Dependency() {
                    };

                    contextConfig.bind(Component.class, ComponentWithInjectConstructorImplementation.class);
                    contextConfig.bind(Dependency.class, dependency);

                    Component instance = contextConfig.getContext().get(Component.class).get();
                    assertNotNull(instance);
                    assertSame(dependency, ((ComponentWithInjectConstructorImplementation) instance).getDependency());
                }

                // TODO: A -> B -> C
                @Test
                public void should_bind_type_to_a_class_with_transitive_dependencies() {
                    contextConfig.bind(Component.class, ComponentWithInjectConstructorImplementation.class);
                    contextConfig.bind(Dependency.class, DependencyWithInjectConstructor.class);
                    contextConfig.bind(String.class, "indirect dependency");

                    Component instance = contextConfig.getContext().get(Component.class).get();
                    assertNotNull(instance);

                    Dependency dependency = ((ComponentWithInjectConstructorImplementation) instance).getDependency();
                    assertNotNull(dependency);

                    assertEquals("indirect dependency", ((DependencyWithInjectConstructor) dependency).getDependency());
                }

                @Test
                public void should_throw_exception_if_multi_inject_constructors_provided() {
                    assertThrows(
                        IllegalComponentException.class,
                        () -> contextConfig.bind(Component.class, ComponentWithMultiInjectConstructorImplementation.class)
                    );
                }

                @Test
                public void should_throw_exception_if_no_inject_nor_no_default_constructor() {
                    assertThrows(
                        IllegalComponentException.class,
                        () -> contextConfig.bind(Component.class, ComponentWithMultiInjectConstructorImplementation.class)
                    );

                }

                // no dependency
                @Test
                public void should_throw_exception_if_dependency_not_found() {
                    contextConfig.bind(Component.class, ComponentWithInjectConstructorImplementation.class);
                    DependencyNotFoundException dependencyNotFoundException = assertThrows(DependencyNotFoundException.class, () -> contextConfig.getContext());
                    assertEquals(Dependency.class, dependencyNotFoundException.getDependency());
                    assertEquals(Component.class, dependencyNotFoundException.getComponent());
                }

                @Test
                public void should_throw_exception_if_transitive_dependency_not_found() {
                    contextConfig.bind(Component.class, ComponentWithInjectConstructorImplementation.class);
                    contextConfig.bind(Dependency.class, DependencyWithInjectConstructor.class);

                    DependencyNotFoundException dependencyNotFoundException = assertThrows(DependencyNotFoundException.class, () -> contextConfig.getContext());
                    assertEquals(String.class, dependencyNotFoundException.getDependency());
                    assertEquals(Dependency.class, dependencyNotFoundException.getComponent());
                }

                @Test
                public void should_throw_exception_if_cycle_dependency() {
                    contextConfig.bind(Component.class, ComponentWithInjectConstructorImplementation.class);
                    contextConfig.bind(Dependency.class, DependencyDependedOnComponent.class);
                    CycleDependenciesFoundException cycleDependenciesFoundException = assertThrows(CycleDependenciesFoundException.class, () -> contextConfig.getContext().get(Component.class));
                    Set<Class<?>> sets = cycleDependenciesFoundException.getComponents();
                    assertEquals(2, sets.size());
                    assertTrue(sets.contains(Component.class));
                    assertTrue(sets.contains(Dependency.class));
                }

                @Test
                public void should_throw_transitive_cycle_dependencies_exception() {
                    contextConfig.bind(Component.class, ComponentWithInjectConstructorImplementation.class);
                    contextConfig.bind(Dependency.class, DependencyDependOnAnotherDependency.class);
                    contextConfig.bind(AnotherDependency.class, AnotherDependencyDependOnComponent.class);
                    assertThrows(CycleDependenciesFoundException.class, () -> contextConfig.getContext().get(Component.class));
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

interface AnotherDependency {

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

class DependencyDependedOnComponent implements Dependency {
    private final Component component;

    @Inject
    public DependencyDependedOnComponent(Component component) {
        this.component = component;
    }
}

class AnotherDependencyDependOnComponent implements AnotherDependency {
    private final Component component;

    @Inject
    public AnotherDependencyDependOnComponent(Component component) {
        this.component = component;
    }
}


class DependencyDependOnAnotherDependency implements Dependency {
    private final AnotherDependency anotherDependency;

    @Inject
    public DependencyDependOnAnotherDependency(AnotherDependency anotherDependency) {
        this.anotherDependency = anotherDependency;
    }
}