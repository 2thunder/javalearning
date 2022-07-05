package di;

import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;

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
                static class ComponentWithFiledInjection {
                    @Inject
                    Dependency dependency;
                }

                static class SubclassWithFieldInjection extends ComponentWithFiledInjection {
                }

                // TODO inject filed
                @Test
                public void should_inject_dependency_via_filed() {
                    Dependency dependency = new Dependency() {
                    };
                    contextConfig.bind(Dependency.class, dependency);
                    contextConfig.bind(ComponentWithFiledInjection.class, ComponentWithFiledInjection.class);
                    ComponentWithFiledInjection componentWithFiledInjection = contextConfig.getContext().get(ComponentWithFiledInjection.class).get();
                    assertSame(dependency, componentWithFiledInjection.dependency);
                }

                @Test
                public void should_subclass_inject_dependency_via_filed() {
                    Dependency dependency = new Dependency() {
                    };
                    contextConfig.bind(Dependency.class, dependency);
                    contextConfig.bind(SubclassWithFieldInjection.class, SubclassWithFieldInjection.class);
                    SubclassWithFieldInjection componentWithFiledInjection = contextConfig.getContext().get(SubclassWithFieldInjection.class).get();
                    assertSame(dependency, componentWithFiledInjection.dependency);
                }

                @Test
                public void should_create_component_with_injection() {
                    Context context = Mockito.mock(Context.class);
                    Dependency dependency = Mockito.mock(Dependency.class);
                    Mockito.when(context.get(eq(Dependency.class))).thenReturn(Optional.of(dependency));
                    ConstructorInjectProvider<ComponentWithFiledInjection> componentConstructorInjectProvider = new ConstructorInjectProvider<>(ComponentWithFiledInjection.class);
                    ComponentWithFiledInjection component = componentConstructorInjectProvider.get(context);
                    assertSame(dependency, component.dependency);
                }

                // todo throw exception if field is final
                // todo provide dependency information for filed injection
                @Test
                public void should_throw_exception_when_filed_dependency_not_found() {
                    contextConfig.bind(ComponentWithFiledInjection.class, ComponentWithFiledInjection.class);
                    assertThrows(DependencyNotFoundException.class, () -> contextConfig.getContext());
                }

                @Test
                @Disabled
                public void should_include_filed_dependency_in_cycle_dependencies() {
                    ConstructorInjectProvider<ComponentWithFiledInjection> provider = new ConstructorInjectProvider<>(ComponentWithFiledInjection.class);
                    assertArrayEquals(new Class<?>[]{Dependency.class}, provider.getDependencies().toArray(Class<?>[]::new));
                }
            }

            @Nested
            public class MethodInjectionNoDepdendency {
                static class InjectMethodWithNoDependency {
                    boolean called = false;

                    public InjectMethodWithNoDependency() {
                    }

                    @Inject
                    void install() {
                        this.called = true;
                    }
                }

                @Test
                public void should_call_inject_method_even_if_no_dependency_declared() {
                    contextConfig.bind(InjectMethodWithNoDependency.class, InjectMethodWithNoDependency.class);
                    InjectMethodWithNoDependency component = contextConfig.getContext().get(InjectMethodWithNoDependency.class).get();
                    assertTrue(component.called);
                }

                static class InjectMethodWithDependency {
                    Dependency dependency;

                    @Inject
                    void install(Dependency dependency) {
                        this.dependency = dependency;
                    }
                }

                @Test
                public void should_inject_dependency_via_inject_method() {
                    Dependency dependency = new Dependency() {
                    };
                    contextConfig.bind(Dependency.class, dependency);
                    contextConfig.bind(InjectMethodWithDependency.class, InjectMethodWithDependency.class);
                    InjectMethodWithDependency component = contextConfig.getContext().get(InjectMethodWithDependency.class).get();
                    assertSame(dependency, component.dependency);
                }

                // TODO inject method with dependencies will be injected
                // TODO override inject method from superclass
                static class SuperClassWithInjectMethods {
                    int superCalled = 0;

                    @Inject
                    void install() {
                        superCalled++;
                    }
                }

                static class SubclassWithInjectMethod extends SuperClassWithInjectMethods {
                    int subCalled = 0;

                    @Inject
                    void installAnother() {
                        subCalled = superCalled + 1;
                    }
                }

                @Test
                public void should_inject_dependencies_via_inject_method_from_superclass() {
                    contextConfig.bind(SubclassWithInjectMethod.class, SubclassWithInjectMethod.class);
                    SubclassWithInjectMethod component = contextConfig.getContext().get(SubclassWithInjectMethod.class).get();
                    assertEquals(2, component.subCalled);
                    assertEquals(1, component.superCalled);
                }

                static class SubclassOverrideSuperclassWithInject extends SuperClassWithInjectMethods {
                    @Inject
                    void install() {
                        super.install();
                    }
                }

                @Test
                public void should_only_call_once_if_subclass_override_inject_method_with_inject() {
                    contextConfig.bind(SubclassOverrideSuperclassWithInject.class, SubclassOverrideSuperclassWithInject.class);
                    Optional<SubclassOverrideSuperclassWithInject> component = contextConfig.getContext().get(SubclassOverrideSuperclassWithInject.class);
                    assertEquals(1, component.get().superCalled);
                }

                static class SubclassOverrideSuperClassWithNoInject extends SuperClassWithInjectMethods {
                    void install() {
                        super.install();
                    }
                }

                @Test
                public void should_not_call_inject_method_if_override_with_no_inject() {
                    contextConfig.bind(SubclassOverrideSuperClassWithNoInject.class, SubclassOverrideSuperClassWithNoInject.class);
                    Optional<SubclassOverrideSuperClassWithNoInject> component = contextConfig.getContext().get(SubclassOverrideSuperClassWithNoInject.class);
                    assertEquals(0, component.get().superCalled);
                }

                // TODO throw exception if type parameter defined

                // TODO include dependencies from inject methods
                @Test
                public void should_include_dependencies_from_inject_method() {
                    ConstructorInjectProvider<InjectMethodWithDependency> provider = new ConstructorInjectProvider<>(InjectMethodWithDependency.class);
                    assertArrayEquals(new Class<?>[]{Dependency.class}, provider.getDependencies().toArray(Class[]::new));
                }
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