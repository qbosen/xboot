package top.abosen.xboot.shared.utility.fp;

import lombok.Value;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static top.abosen.xboot.shared.utility.fp.FluentPropertySetter.*;

class FluentPropertySetterTest {

    @Test
    void should_retrieve_value_from_map() {
        // Setup
        final Map<String, String> source = new HashMap<>();
        source.put("key", "value");
        final Function<String, String> idGen = val -> val;

        // Run the test
        final Function<String, String> result = closureProvider(source, idGen);

        // Verify the results
        assertEquals("value", result.apply("key"));
        assertNull(result.apply("key_not_exist_in_map"));
    }

    @Test
    void should_retrieve_value_from_collection() {
        // Setup
        final Function<String, String> sourceId = val -> val.substring(0, 1); // 首字母作为id
        final Function<String, String> destId = val -> val;

        // Run the test
        final Function<String, String> result = closureProvider(Arrays.asList("foo", "bar"), sourceId, destId);

        // Verify the results
        assertEquals("foo", result.apply("f"));
        assertEquals("bar", result.apply("b"));
        assertNull(result.apply("_"));
    }

    @Test
    void should_retrieve_last_value_from_collection_if_source_id_duplicated() {
        // Setup
        final Function<String, String> sourceId = val -> val.substring(0, 1); // 首字母作为id
        final Function<String, String> destId = val -> val;

        // Run the test
        final Function<String, String> result = closureProvider(Arrays.asList("foo", "far", "father"), sourceId, destId);

        // Verify the results
        assertEquals("father", result.apply("f"));
    }

    @Test
    void should_behave_same_with_collection_provider_when_invoke() {
        // Setup
        final Function<String, String> sourceId = val -> val.substring(0, 1); // 首字母作为id
        final Function<String, String> destId = val -> val;

        // Run the test
        final Supplier<Function<String, String>> result = lazyProvider(() -> Arrays.asList("foo", "bar", "father"), sourceId, destId);

        // Verify the results
        assertEquals("father", result.get().apply("f"));
        assertEquals("bar", result.get().apply("b"));
        assertNull(result.get().apply("_"));
    }

    @Test
    void should_behave_lazy() {
        // Setup
        final Function<String, String> sourceId = val -> val;
        final Function<String, String> destId = val -> val;


        // Run the test
        AtomicReference<Supplier<Function<String, String>>> result = new AtomicReference<>();
        // Verify the results
        assertDoesNotThrow(() -> result.set(
                lazyProvider(() -> {
                    throw new RuntimeException("lazy exception");
                }, sourceId, destId)));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> result.get().get().apply("foo"));
        assertEquals("lazy exception", exception.getMessage());
    }

    @SuppressWarnings("unchecked")
    @Nested
    class TransferTest {
        @Value
        class Source {
            String key;
        }
        @Value
        class Dest{
            Integer value;
        }

        Function<Source, String> getter = mock(Function.class);
        BiConsumer<Dest, Integer> setter = mock(BiConsumer.class);
        Function<String, Integer> mapper = mock(Function.class);
        BiPredicate<Source, Dest> prev = mock(BiPredicate.class);
        BiPredicate<Dest, Integer> post = mock(BiPredicate.class);

        @Test
        void should_not_invoke_getter_setter_mapper_post_if_prev_condition_false() {
            // Setup
            when(prev.test(any(), any())).thenReturn(false);
            Source source = new Source("key");
            Dest dest = new Dest(1);
            // Run the test
            transfer(getter, setter, mapper, prev, post).accept(source, dest);

            // Verify the results
            verify(prev,times(1)).test(same(source), same(dest));
            verifyNoMoreInteractions(getter, setter, mapper, post);
        }

        @Test
        void should_invoke_setter() {
            // Setup
            when(prev.test(any(), any())).thenReturn(true);
            when(post.test(any(), any())).thenReturn(true);
            Dest dest = new Dest(0);
            when(mapper.apply(any())).thenReturn(1);
            // Run the test
            transfer(getter, setter, mapper, prev, post).accept(new Source("value"), dest);
            // Verify
            verify(setter,times(1)).accept(same(dest), eq(1));
        }

    }

}
