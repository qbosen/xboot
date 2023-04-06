package top.abosen.xboot.objectdiffer;

import de.danielbechler.diff.access.Instances;
import de.danielbechler.diff.access.RootAccessor;
import de.danielbechler.diff.comparison.ComparisonStrategy;
import de.danielbechler.diff.comparison.ComparisonStrategyResolver;
import de.danielbechler.diff.differ.DifferDispatcher;
import de.danielbechler.diff.identity.EqualsIdentityStrategy;
import de.danielbechler.diff.identity.IdentityStrategy;
import de.danielbechler.diff.identity.IdentityStrategyResolver;
import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.path.NodePath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.*;

/**
 * @author qiubaisen
 * @since 2023/1/30
 */
public class ArrayDifferTest {
    ComparisonStrategyResolver comparisonStrategyResolver;
    ComparisonStrategy comparisonStrategy;
    IdentityStrategyResolver identityStrategyResolver;
    IdentityStrategy identityStrategy;

    DifferDispatcher differDispatcher;
    Instances instances;

    ArrayDiffer arrayDiffer;
    DiffNode node;
    Object[] baseArray;
    Object[] workingArray;

    @BeforeEach
    void setup() {
        comparisonStrategyResolver = mock(ComparisonStrategyResolver.class);
        comparisonStrategy = mock(ComparisonStrategy.class);
        identityStrategyResolver = mock(IdentityStrategyResolver.class);
        identityStrategy = mock(IdentityStrategy.class);

        differDispatcher = mock(DifferDispatcher.class);
        instances = mock(Instances.class);


        arrayDiffer = new ArrayDiffer(differDispatcher, comparisonStrategyResolver, identityStrategyResolver);
        baseArray = new String[1];
        workingArray = new String[1];

        when(instances.getSourceAccessor()).thenReturn(RootAccessor.getInstance());
        Mockito.<Class<?>>when(instances.getType()).thenReturn(Object[].class);
        when(instances.getBase()).thenReturn(baseArray);
        when(instances.getWorking()).thenReturn(workingArray);
    }

    @ParameterizedTest
    @ValueSource(classes = {int[].class, Object[].class, String[].class, Integer[][].class})
    void should_accept_array_types(Class<?> type) {
        assertThat(arrayDiffer.accepts(type)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(classes = {int.class, Object.class, List.class})
    void should_reject_non_array_types(Class<?> type) {
        assertThat(arrayDiffer.accepts(type)).isFalse();
    }

    @Test
    void should_return_untouched_if_instances_are_same() {
        when(instances.areSame()).thenReturn(true);
        node = arrayDiffer.compare(DiffNode.ROOT, instances);
        assertThat(node.getState()).isEqualTo(DiffNode.State.UNTOUCHED);
    }

    @Test
    void should_return_added_if_instances_has_been_added() {
        when(instances.hasBeenAdded()).thenReturn(true);
        node = arrayDiffer.compare(DiffNode.ROOT, instances);
        assertThat(node.getState()).isEqualTo(DiffNode.State.ADDED);
    }

    @Test
    void should_return_removed_if_instances_has_been_removed() {
        when(instances.hasBeenRemoved()).thenReturn(true);
        node = arrayDiffer.compare(DiffNode.ROOT, instances);
        assertThat(node.getState()).isEqualTo(DiffNode.State.REMOVED);
    }

    @Test
    void should_delegate_added_items_to_dispatcher_if_instances_has_been_added() {
        when(instances.hasBeenAdded()).thenReturn(true);
        when(instances.getWorking()).thenReturn(new String[]{"foo"});
        when(identityStrategyResolver.resolveIdentityStrategy(any())).thenReturn(identityStrategy);

        node = arrayDiffer.compare(DiffNode.ROOT, instances);

        verify(differDispatcher, times(1)).dispatch(
                isNotNull(), same(instances), isA(ArrayItemAccessor.class)
        );
    }

    @Test
    void should_delegate_removed_items_to_dispatcher_if_instances_has_been_removed() {
        when(instances.hasBeenRemoved()).thenReturn(true);
        when(instances.getBase()).thenReturn(new String[]{"foo"});
        when(identityStrategyResolver.resolveIdentityStrategy(any())).thenReturn(identityStrategy);

        node = arrayDiffer.compare(DiffNode.ROOT, instances);

        verify(differDispatcher, times(1)).dispatch(
                isNotNull(), same(instances), isA(ArrayItemAccessor.class)
        );
    }

    @Test
    void should_use_comparison_strategy_if_available() {
        when(comparisonStrategyResolver.resolveComparisonStrategy(any())).thenReturn(comparisonStrategy);
        node = arrayDiffer.compare(DiffNode.ROOT, instances);
        verify(comparisonStrategy, times(1)).compare(
                argThat(node -> node.getPath().matches(NodePath.withRoot())),
                argThat(type -> type == Object[].class),
                same(workingArray),
                same(baseArray));
    }

    @ParameterizedTest
    @CsvSource(delimiterString = "|", nullValues = "EMPTY",
            textBlock = """
                    # working   |   base
                    added       |   EMPTY
                    known       |   known
                    EMPTY       |   removed
                    """)
    void should_delegate_items_to_dispatcher_when_compare_deep(
            @ConvertWith(ArgumentConverters.ToArray.class) String[] working,
            @ConvertWith(ArgumentConverters.ToArray.class) String[] base) {
        when(instances.getWorking()).thenReturn(working);
        when(instances.getBase()).thenReturn(base);
        when(identityStrategyResolver.resolveIdentityStrategy(any())).thenReturn(EqualsIdentityStrategy.getInstance());

        node = arrayDiffer.compare(DiffNode.ROOT, instances);
        verify(differDispatcher, times(1)).dispatch(
                argThat(node -> node.getPath().matches(NodePath.withRoot())),
                same(instances),
                isA(ArrayItemAccessor.class)
        );
    }
}
