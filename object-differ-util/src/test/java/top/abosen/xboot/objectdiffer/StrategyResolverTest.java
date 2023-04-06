package top.abosen.xboot.objectdiffer;

import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.comparison.ComparableComparisonStrategy;
import de.danielbechler.diff.comparison.ComparisonStrategy;
import de.danielbechler.diff.comparison.ComparisonStrategyResolver;
import de.danielbechler.diff.identity.EqualsIdentityStrategy;
import de.danielbechler.diff.identity.IdentityStrategy;
import de.danielbechler.diff.identity.IdentityStrategyResolver;
import de.danielbechler.diff.node.DiffNode;
import lombok.Value;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import top.abosen.xboot.objectdiffer.DelegateComparisonService.EqualsFieldComparisonStrategy;
import top.abosen.xboot.objectdiffer.DelegateComparisonService.EqualsMethodComparisonStrategy;
import top.abosen.xboot.objectdiffer.DelegateIdentityService.IdentityFieldIdentityStrategy;
import top.abosen.xboot.objectdiffer.DelegateIdentityService.IdentityMethodIdentityStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * @author qiubaisen
 * @since 2023/1/15
 */
class StrategyResolverTest {


    @Nested
    class ComparisonStrategyTest {

        static void resolveStrategy(Class<?> valueType, Class<? extends ComparisonStrategy> expectStrategy) {
            ComparisonStrategyResolver strategyResolver = new DelegateComparisonService(mock(ObjectDifferBuilder.class));
            ComparisonStrategy comparisonStrategy = strategyResolver.resolveComparisonStrategy(DiffNode.newRootNodeWithType(valueType));
            assertNotNull(comparisonStrategy);
            assertInstanceOf(expectStrategy, comparisonStrategy);
        }

        @TestFactory
        List<DynamicTest> should_resolve_comparison_strategy() {
            List<DynamicTest> tests = new ArrayList<>();
            tests.add(DynamicTest.dynamicTest("method strategy", () -> resolveStrategy(ObjectWithEqualsMethod.class, EqualsMethodComparisonStrategy.class)));
            tests.add(DynamicTest.dynamicTest("field strategy", () -> resolveStrategy(ObjectWithEqualsField.class, EqualsFieldComparisonStrategy.class)));
            tests.add(DynamicTest.dynamicTest("both field and method, method strategy first", () -> resolveStrategy(ObjectWithEqualsMethodAndField.class, EqualsMethodComparisonStrategy.class)));
            tests.add(DynamicTest.dynamicTest("simple type", () -> resolveStrategy(Integer.class, ComparableComparisonStrategy.class)));
            return tests;
        }

        @ParameterizedTest(name = "{2}")
        @MethodSource
        void should_compare_with_annotated_field(ObjectWithSomeEqualsField source, ObjectWithSomeEqualsField target, String context) {
            DelegateComparisonService comparisonService = new DelegateComparisonService(mock(ObjectDifferBuilder.class));
            DiffNode node = DiffNode.newRootNodeWithType(ObjectWithSomeEqualsField.class);
            ComparisonStrategy comparisonStrategy = comparisonService.resolveComparisonStrategy(node);

            comparisonStrategy.compare(node, ObjectWithSomeEqualsField.class, target, source);
            assertEquals(source.expectEquals(target), node.isUntouched());
        }

        static Stream<Arguments> should_compare_with_annotated_field() {
            List<Arguments> result = new ArrayList<>();
            boolean[] equality = new boolean[]{false, true};
            for (boolean firstNameEquality : equality) {
                for (boolean secondNameEquality : equality) {
                    for (boolean emailEquality : equality) {
                        result.add(Arguments.of(
                                new ObjectWithSomeEqualsField("firstName", "secondName", "email"),
                                new ObjectWithSomeEqualsField(firstNameEquality ? "firstName" : "firstNameDiff", secondNameEquality ? "secondName" : "secondNameDiff", emailEquality ? "email" : "emailDiff"),
                                "firstName" + (firstNameEquality ? " EQ " : " NE ") + ", " + "secondName" + (secondNameEquality ? " EQ " : " NE ") + ", " + "email" + (emailEquality ? " EQ " : " NE ")
                        ));
                    }
                }
            }
            return result.stream();
        }

        @Value
        static class ObjectWithSomeEqualsField {

            @DiffEquals
            String firstName;
            @DiffEquals
            String secondName;
            String email;

            public boolean expectEquals(ObjectWithSomeEqualsField other) {
                return Objects.equals(firstName, other.firstName) && Objects.equals(secondName, other.secondName);
            }
        }

    }


    @Nested
    class IdentityStrategyTest {

        static void resolveStrategy(Class<?> valueType, Class<? extends IdentityStrategy> expectStrategy) {
            IdentityStrategyResolver strategyResolver = new DelegateIdentityService(mock(ObjectDifferBuilder.class));
            IdentityStrategy identityStrategy = strategyResolver.resolveIdentityStrategy(DiffNode.newRootNodeWithType(valueType));
            assertNotNull(identityStrategy);
            assertInstanceOf(expectStrategy, identityStrategy);
        }

        @TestFactory
        List<DynamicTest> should_resolve_identity_strategy() {
            List<DynamicTest> tests = new ArrayList<>();
            tests.add(DynamicTest.dynamicTest("method strategy", () -> resolveStrategy(ObjectWithIdentityMethod.class, IdentityMethodIdentityStrategy.class)));
            tests.add(DynamicTest.dynamicTest("field strategy", () -> resolveStrategy(ObjectWithIdentityField.class, IdentityFieldIdentityStrategy.class)));
            tests.add(DynamicTest.dynamicTest("both field and method, method strategy first", () -> resolveStrategy(ObjectWithIdentityMethodAndField.class, IdentityMethodIdentityStrategy.class)));
            tests.add(DynamicTest.dynamicTest("simple type", () -> resolveStrategy(Integer.class, EqualsIdentityStrategy.class)));
            return tests;
        }

        @ParameterizedTest(name = "{2}")
        @MethodSource
        void should_identity_with_annotated_field(ObjectWithSomeIdentityField source, ObjectWithSomeIdentityField target, String context) {
            IdentityStrategyResolver strategyResolver = new DelegateIdentityService(mock(ObjectDifferBuilder.class));
            DiffNode node = DiffNode.newRootNodeWithType(ObjectWithSomeIdentityField.class);
            IdentityStrategy identityStrategy = strategyResolver.resolveIdentityStrategy(node);

            assertEquals(source.expectSame(target), identityStrategy.equals(target, source));
        }

        static Stream<Arguments> should_identity_with_annotated_field() {
            List<Arguments> result = new ArrayList<>();
            boolean[] equality = new boolean[]{false, true};
            for (boolean firstNameEquality : equality) {
                for (boolean secondNameEquality : equality) {
                    for (boolean emailEquality : equality) {
                        result.add(Arguments.of(
                                new ObjectWithSomeIdentityField("firstName", "secondName", "email"),
                                new ObjectWithSomeIdentityField(firstNameEquality ? "firstName" : "firstNameDiff", secondNameEquality ? "secondName" : "secondNameDiff", emailEquality ? "email" : "emailDiff"),
                                "firstName" + (firstNameEquality ? " EQ " : " NE ") + ", " + "secondName" + (secondNameEquality ? " EQ " : " NE ") + ", " + "email" + (emailEquality ? " EQ " : " NE ")
                        ));
                    }
                }
            }
            return result.stream();
        }

        @Value
        static class ObjectWithSomeIdentityField {

            @DiffIdentity
            String firstName;
            @DiffIdentity
            String secondName;
            String email;

            public boolean expectSame(ObjectWithSomeIdentityField other) {
                return Objects.equals(firstName, other.firstName) && Objects.equals(secondName, other.secondName);
            }
        }

    }

    @Value
    static class ObjectWithEqualsField {
        @DiffEquals
        String no;
    }

    @Value
    static class ObjectWithEqualsMethod {
        String no;

        @DiffEquals
        private String equals() {
            return no;
        }
    }

    @Value
    static class ObjectWithEqualsMethodAndField {
        @DiffEquals
        String id;

        @DiffEquals
        String equals() {
            return id;
        }
    }

    @Value
    static class ObjectWithIdentityField {
        @DiffIdentity
        String no;
    }

    @Value
    static class ObjectWithIdentityMethod {
        String no;

        @DiffIdentity
        private String equals() {
            return no;
        }
    }

    @Value
    static class ObjectWithIdentityMethodAndField {
        @DiffIdentity
        String id;

        @DiffIdentity
        String equals() {
            return id;
        }
    }


    @Value
    static class ObjectPure {
        String foo;
        String bar;
    }

}