package top.abosen.xboot.objectdiffer;

import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.comparison.ComparableComparisonStrategy;
import de.danielbechler.diff.comparison.ComparisonStrategy;
import de.danielbechler.diff.node.DiffNode;
import lombok.Value;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import top.abosen.xboot.objectdiffer.DelegateComparisonService.EqualsFieldComparisonStrategy;
import top.abosen.xboot.objectdiffer.DelegateComparisonService.EqualsMethodComparisonStrategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * @author qiubaisen
 * @date 2023/1/15
 */
class DelegateComparisonServiceTest {


    static void resolveStrategy(Class<?> valueType, Class<? extends ComparisonStrategy> expectStrategy) {
        DelegateComparisonService comparisonService = new DelegateComparisonService(mock(ObjectDifferBuilder.class));
        ComparisonStrategy comparisonStrategy = comparisonService.resolveComparisonStrategy(DiffNode.newRootNodeWithType(valueType));
        assertNotNull(comparisonStrategy);
        assertInstanceOf(expectStrategy, comparisonStrategy);
    }

    @TestFactory
    List<DynamicTest> should_resolve_comparison_strategy() {
        List<DynamicTest> tests = new ArrayList<>();
        tests.add(DynamicTest.dynamicTest("method strategy", () -> resolveStrategy(ObjectWithEqualsMethod.class, EqualsMethodComparisonStrategy.class)));
        tests.add(DynamicTest.dynamicTest("field strategy", () -> resolveStrategy(ObjectWithEqualsField.class, EqualsFieldComparisonStrategy.class)));
        tests.add(DynamicTest.dynamicTest("both field and method, method strategy first", () -> resolveStrategy(ObjectWithEqualsMethodAndField.class, EqualsMethodComparisonStrategy.class)));
        tests.add(DynamicTest.dynamicTest("simple type comparable", () -> resolveStrategy(Integer.class, ComparableComparisonStrategy.class)));
        return tests;
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
    static class ObjectComparable implements Comparator<ObjectComparable> {
        @Override
        public int compare(ObjectComparable o1, ObjectComparable o2) {
            return 0;
        }
    }


    @Value
    static class ObjectPure {
        String foo;
        String bar;
    }


    @ParameterizedTest(name = "{2}")
    @MethodSource
    void should_compare_with_annotated_field(ObjectWithSomeDiffFields source, ObjectWithSomeDiffFields target, String context) {
        DelegateComparisonService comparisonService = new DelegateComparisonService(mock(ObjectDifferBuilder.class));
        DiffNode node = DiffNode.newRootNodeWithType(ObjectWithSomeDiffFields.class);
        ComparisonStrategy comparisonStrategy = comparisonService.resolveComparisonStrategy(node);

        comparisonStrategy.compare(node, ObjectWithSomeDiffFields.class, target, source);
        assertEquals(source.expectEquals(target), node.isUntouched());
    }

    static Stream<Arguments> should_compare_with_annotated_field() {
        List<Arguments> result = new ArrayList<>();
        boolean[] equality = new boolean[]{false, true};
        for (boolean firstNameEquality : equality) {
            for (boolean secondNameEquality : equality) {
                for (boolean emailEquality : equality) {
                    result.add(Arguments.of(
                            new ObjectWithSomeDiffFields("firstName", "secondName", "email"),
                            new ObjectWithSomeDiffFields(firstNameEquality ? "firstName" : "firstNameDiff", secondNameEquality ? "secondName" : "secondNameDiff", emailEquality ? "email" : "emailDiff"),
                            "firstName" + (firstNameEquality ? " EQ " : " NE ") + ", " + "secondName" + (secondNameEquality ? " EQ " : " NE ") + ", " + "email" + (emailEquality ? " EQ " : " NE ")
                    ));
                }
            }
        }
        return result.stream();
    }

    @Value
    static class ObjectWithSomeDiffFields {
        @DiffEquals
        String firstName;
        @DiffEquals
        String secondName;

        String email;

        public boolean expectEquals(ObjectWithSomeDiffFields other) {
            return Objects.equals(firstName, other.firstName) && Objects.equals(secondName, other.secondName);
        }
    }

}