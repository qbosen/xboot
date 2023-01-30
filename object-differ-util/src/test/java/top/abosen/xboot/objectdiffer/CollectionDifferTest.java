package top.abosen.xboot.objectdiffer;

import de.danielbechler.diff.access.CollectionItemAccessor;
import de.danielbechler.diff.access.Instances;
import de.danielbechler.diff.access.RootAccessor;
import de.danielbechler.diff.comparison.ComparisonStrategy;
import de.danielbechler.diff.comparison.ComparisonStrategyResolver;
import de.danielbechler.diff.differ.CollectionDiffer;
import de.danielbechler.diff.differ.DifferDispatcher;
import de.danielbechler.diff.identity.EqualsIdentityStrategy;
import de.danielbechler.diff.identity.IdentityStrategy;
import de.danielbechler.diff.identity.IdentityStrategyResolver;
import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.path.NodePath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.TypedArgumentConverter;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author qiubaisen
 * @date 2023/1/29
 */
public class CollectionDifferTest {

    ComparisonStrategyResolver comparisonStrategyResolver;

    ComparisonStrategy comparisonStrategy;

    IdentityStrategyResolver identityStrategyResolver;

    DifferDispatcher differDispatcher;

    Instances instances;

    CollectionDiffer collectionDiffer;
    DiffNode node;
    Collection<String> baseCollection;
    Collection<String> workingCollection;
    IdentityStrategy identityStrategy;

    @BeforeEach
    void setup() {
        comparisonStrategyResolver = mock(ComparisonStrategyResolver.class);
        comparisonStrategy = mock(ComparisonStrategy.class);
        identityStrategyResolver = mock(IdentityStrategyResolver.class);
        identityStrategy = mock(IdentityStrategy.class);

        differDispatcher = mock(DifferDispatcher.class);
        instances = mock(Instances.class);


        collectionDiffer = new CollectionDiffer(differDispatcher, comparisonStrategyResolver, identityStrategyResolver);
        baseCollection = new HashSet<>();
        workingCollection = new HashSet<>();

        when(instances.getSourceAccessor()).thenReturn(RootAccessor.getInstance());
        Mockito.<Class<?>>when(instances.getType()).thenReturn(List.class);
        when(instances.getBase()).thenReturn(baseCollection);
        when(instances.getBase(any())).thenReturn(baseCollection);
        when(instances.getWorking()).thenReturn(workingCollection);
        when(instances.getWorking(any())).thenReturn(workingCollection);
    }

    @ParameterizedTest
    @ValueSource(classes = {Collection.class, List.class, Queue.class, Set.class})
    void should_accept_collection_types(Class<?> type) {
        assertThat(collectionDiffer.accepts(type)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(classes = {Object.class, Date.class, int.class})
    void should_reject_non_collection_types(Class<?> type) {
        assertThat(collectionDiffer.accepts(type)).isFalse();
    }

    @Test
    void should_return_untouched_if_instances_are_same() {
        when(instances.areSame()).thenReturn(true);
        node = collectionDiffer.compare(DiffNode.ROOT, instances);
        assertThat(node.getState()).isEqualTo(DiffNode.State.UNTOUCHED);
    }

    @Test
    void should_return_added_if_instances_has_been_added() {
        when(instances.hasBeenAdded()).thenReturn(true);
        node = collectionDiffer.compare(DiffNode.ROOT, instances);
        assertThat(node.getState()).isEqualTo(DiffNode.State.ADDED);
    }

    @Test
    void should_return_removed_if_instances_has_been_removed() {
        when(instances.hasBeenRemoved()).thenReturn(true);
        node = collectionDiffer.compare(DiffNode.ROOT, instances);
        assertThat(node.getState()).isEqualTo(DiffNode.State.REMOVED);
    }

    @Test
    void should_delegate_added_items_to_dispatcher_if_instances_has_been_added() {
        when(instances.hasBeenAdded()).thenReturn(true);
        when(instances.getWorking(eq(Collection.class))).thenReturn(List.of("foo"));
        when(identityStrategyResolver.resolveIdentityStrategy(any())).thenReturn(identityStrategy);

        node = collectionDiffer.compare(DiffNode.ROOT, instances);

        verify(differDispatcher, times(1)).dispatch(
                isNotNull(), same(instances), isA(CollectionItemAccessor.class)
        );
    }

    @Test
    void should_delegate_removed_items_to_dispatcher_if_instances_has_been_removed() {
        when(instances.hasBeenRemoved()).thenReturn(true);
        when(instances.getBase(eq(Collection.class))).thenReturn(List.of("foo"));
        when(identityStrategyResolver.resolveIdentityStrategy(any())).thenReturn(identityStrategy);

        node = collectionDiffer.compare(DiffNode.ROOT, instances);

        verify(differDispatcher, times(1)).dispatch(
                isNotNull(), same(instances), isA(CollectionItemAccessor.class)
        );
    }

    @Test
    void should_use_comparison_strategy_if_available() {
        when(comparisonStrategyResolver.resolveComparisonStrategy(any())).thenReturn(comparisonStrategy);
        node = collectionDiffer.compare(DiffNode.ROOT, instances);
        verify(comparisonStrategy, times(1)).compare(
                argThat(node -> node.getPath().matches(NodePath.withRoot())),
                argThat(type -> type == List.class),
                same(workingCollection),
                same(baseCollection));
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
            @ConvertWith(ToCollectionArgumentConverter.class) Collection<String> working,
            @ConvertWith(ToCollectionArgumentConverter.class) Collection<String> base) {
        when(instances.getWorking(eq(Collection.class))).thenReturn(working);
        when(instances.getBase(eq(Collection.class))).thenReturn(base);
        when(identityStrategyResolver.resolveIdentityStrategy(any())).thenReturn(EqualsIdentityStrategy.getInstance());

        node = collectionDiffer.compare(DiffNode.ROOT, instances);
        verify(differDispatcher, times(1)).dispatch(
                argThat(node -> node.getPath().matches(NodePath.withRoot())),
                same(instances),
                isA(CollectionItemAccessor.class)
        );
    }

    static class ToCollectionArgumentConverter extends TypedArgumentConverter<String, Collection> {

        public ToCollectionArgumentConverter() {
            super(String.class, Collection.class);
        }

        @Override
        protected Collection<String> convert(String source) throws ArgumentConversionException {
            if (source == null) return Collections.emptyList();
            return Arrays.stream(source.split(",")).collect(Collectors.toList());
        }
    }

}
