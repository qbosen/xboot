package top.abosen.xboot.extensionfield.extension;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import top.abosen.xboot.extensionfield.valueholder.ValueHolder;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author qiubaisen
 * @since 2023/3/2
 */
class SwitchExtensionFieldTest {
    @Test
    void should_valid_false_if_options_is_null() {
        SwitchExtensionField field = SwitchExtensionField.builder().key("key").options(null).build();
        assertThat(field.validMessage()).isPresent().hasValue("字段列表不能为空");
    }

    @Test
    void should_valid_false_if_options_is_empty() {
        SwitchExtensionField field = SwitchExtensionField.builder().key("key").options(Maps.newHashMap()).build();
        assertThat(field.validMessage()).isPresent().hasValue("字段列表不能为空");
    }

    @Test
    void should_valid_false_if_options_contains_blank_key() {
        ExtensionField nested = mock(ExtensionField.class);
        when(nested.getKey()).thenReturn("foo");
        SwitchExtensionField field = SwitchExtensionField.builder().key("key")
                .options(ImmutableMap.of(" ", nested)).build();

        assertThat(field.validMessage()).isPresent().hasValue("选项不能为空");
    }

    @Test
    void should_valid_nested_fields() {
        ExtensionField nested = mock(ExtensionField.class);
        when(nested.getKey()).thenReturn("foo");
        SwitchExtensionField field = SwitchExtensionField.builder().key("key")
                .options(ImmutableMap.of("opt1", nested, "opt2", nested)).build();

        field.validMessage();
        verify(nested, times(2)).validMessage();
    }

    @ParameterizedTest
    @MethodSource
    @NullSource
    void should_return_false_if_check_value_type_not_map(Object value) {
        ValueHolder holder = mock(ValueHolder.class);
        when(holder.get()).thenReturn(value);

        SwitchExtensionField field = SwitchExtensionField.builder().build();

        assertThat(field.checkValue(holder)).isFalse();
    }

    static Stream<Object> should_return_false_if_check_value_type_not_map() {
        return Stream.of(1, "str", Arrays.asList(3.14, 0.0), new long[]{1L, 2L});
    }

    @Test
    void should_return_false_if_check_value_map_is_empty() {
        ValueHolder holder = mock(ValueHolder.class);
        when(holder.get()).thenReturn(Maps.newHashMap());

        SwitchExtensionField field = SwitchExtensionField.builder().options(Maps.newHashMap()).build();

        assertThat(field.checkValue(holder)).isFalse();
    }

    @Test
    void should_return_false_if_check_value_key_not_in_options() {
        ValueHolder holder = mock(ValueHolder.class);
        when(holder.get()).thenReturn(ImmutableMap.of("k1", "v1", "k2", "v2"));
        ExtensionField nested = mock(ExtensionField.class);

        SwitchExtensionField field = SwitchExtensionField.builder().options(ImmutableMap.of("opt1", nested)).build();

        assertThat(field.checkValue(holder)).isFalse();
    }

    @Test
    void should_check_value_for_nested_option_field() {
        ValueHolder holder = mock(ValueHolder.class);
        when(holder.get()).thenReturn(ImmutableMap.of("opt", "v1"));
        ExtensionField nested = mock(ExtensionField.class);
        when(nested.checkValue(any())).thenReturn(true);

        SwitchExtensionField field = SwitchExtensionField.builder().options(ImmutableMap.of("opt", nested)).build();

        assertThat(field.checkValue(holder)).isTrue();
        verify(nested, times(1)).checkValue(argThat(h -> "v1".equals(h.get())));
    }
}