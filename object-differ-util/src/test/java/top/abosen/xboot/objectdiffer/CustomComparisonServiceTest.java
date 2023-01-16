package top.abosen.xboot.objectdiffer;

import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.comparison.ComparisonService;
import de.danielbechler.diff.comparison.ComparisonStrategy;
import de.danielbechler.diff.node.DiffNode;
import lombok.Value;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * @author qiubaisen
 * @date 2023/1/15
 */
class CustomComparisonServiceTest {
    @Test
    void should_return_diff_id_strategy_if_annotation_present() {
        CustomComparisonService comparisonService = new CustomComparisonService(mock(ObjectDifferBuilder.class));

        ComparisonStrategy comparisonStrategy = comparisonService.resolveComparisonStrategy(DiffNode.newRootNodeWithType(ObjectWithDiffFieldId.class));
        assertNotNull(comparisonStrategy);
        assertInstanceOf(CustomComparisonService.DiffFieldIdAnnotationComparisonStrategy.class, comparisonStrategy);
    }

    @Value
    static class ObjectWithDiffFieldId {
        @DiffField.Id
        String no;
    }

    @Test
    void should_return_default_strategy_if_annotation_absent() {
        ObjectDifferBuilder builder = mock(ObjectDifferBuilder.class);
        ComparisonService defaultComparisonService = new ComparisonService(builder);
        CustomComparisonService customComparisonService = new CustomComparisonService(builder);

        DiffNode diffNode = DiffNode.newRootNodeWithType(ObjectWithoutDiffFieldId.class);
        assertSame(defaultComparisonService.resolveComparisonStrategy(diffNode), customComparisonService.resolveComparisonStrategy(diffNode));
    }

    @Value
    static class ObjectWithoutDiffFieldId {
        String foo;
        String bar;
    }

    @ParameterizedTest(name = "{2}")
    @MethodSource
    void should_compare_with_annotated_field(ObjectWithSomeDiffFieldId source, ObjectWithSomeDiffFieldId target, String context) {
        CustomComparisonService comparisonService = new CustomComparisonService(mock(ObjectDifferBuilder.class));
        DiffNode node = DiffNode.newRootNodeWithType(ObjectWithSomeDiffFieldId.class);
        ComparisonStrategy comparisonStrategy = comparisonService.resolveComparisonStrategy(node);

        comparisonStrategy.compare(node, ObjectWithSomeDiffFieldId.class, target, source);
        assertEquals(source.expectEquals(target), node.isUntouched());
    }

    static Stream<Arguments> should_compare_with_annotated_field() {
        List<Arguments> result = new ArrayList<>();
        boolean[] equality = new boolean[]{false, true};
        for (boolean firstNameEquality : equality) {
            for (boolean secondNameEquality : equality) {
                for (boolean emailEquality : equality) {
                    result.add(Arguments.of(
                            new ObjectWithSomeDiffFieldId("firstName", "secondName", "email"),
                            new ObjectWithSomeDiffFieldId(firstNameEquality ? "firstName" : "firstNameDiff", secondNameEquality ? "secondName" : "secondNameDiff", emailEquality ? "email" : "emailDiff"),
                            "firstName" + (firstNameEquality ? " EQ " : " NE ") + ", " + "secondName" + (secondNameEquality ? " EQ " : " NE ") + ", " + "email" + (emailEquality ? " EQ " : " NE ")
                    ));
                }
            }
        }
        return result.stream();
    }

    @Value
    static class ObjectWithSomeDiffFieldId {
        @DiffField.Id
        String firstName;
        @DiffField.Id
        String secondName;

        String email;

        public boolean expectEquals(ObjectWithSomeDiffFieldId other) {
            return Objects.equals(firstName, other.firstName) && Objects.equals(secondName, other.secondName);
        }
    }

}