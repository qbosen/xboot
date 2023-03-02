package top.abosen.xboot.extensionfield.extension;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentMatchers;
import top.abosen.xboot.extensionfield.valueholder.ValueHolder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

/**
 * @author qiubaisen
 * @date 2023/3/2
 */
class MapExtensionFieldTest {

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " ", "\t", "\n", " \t\n"})
    void should_valid_empty_key(String key) {
        MapExtensionField field = MapExtensionField.builder().key(key).build();
        assertThat(field.validMessage()).isPresent()
                .hasValue("字段key不能为空");
    }


    @Test
    void should_valid_empty_fields() {
        MapExtensionField field = MapExtensionField.builder().key("key").fields(ListUtil.of()).build();
        assertThat(field.validMessage()).isPresent()
                .hasValue("字段列表不能为空");
    }


    @Test
    void should_valid_duplicate_key() {
        ExtensionField field1 = mock(ExtensionField.class);
        ExtensionField field2 = mock(ExtensionField.class);
        when(field1.getKey()).thenReturn("foo");
        when(field2.getKey()).thenReturn("foo");
        MapExtensionField field = MapExtensionField.builder().key("key")
                .fields(ListUtil.of(field1, field2)).build();

        assertThat(field.validMessage()).isPresent()
                .hasValue("字段key不能重复");
    }

    @Test
    void should_valid_nested_fields() {
        ExtensionField nested = mock(ExtensionField.class);
        when(nested.getKey()).thenReturn("foo");
        MapExtensionField field = MapExtensionField.builder().key("key")
                .fields(ListUtil.of(nested)).build();

        field.validMessage();
        verify(nested, times(1)).validMessage();
    }

    @ParameterizedTest
    @MethodSource
    @NullSource
    void should_return_false_if_check_value_type_not_map(Object value) {
        ValueHolder holder = mock(ValueHolder.class);
        when(holder.get()).thenReturn(value);

        MapExtensionField field = MapExtensionField.builder().build();

        assertThat(field.checkValue(holder)).isFalse();
    }

    static Stream<Object> should_return_false_if_check_value_type_not_map() {
        return Stream.of(1, "str", Arrays.asList(3.14, 0.0), new long[]{1L, 2L});
    }

    @Test
    void should_check_value_for_nested_fields(){
        ExtensionField nested = mock(ExtensionField.class);
        MapExtensionField field = MapExtensionField.builder().fields(ListUtil.of(nested)).build();
        ValueHolder holder = mock(ValueHolder.class);
        when(holder.get()).thenReturn(ImmutableMap.of());

        field.checkValue(holder);
        verify(nested, times(1)).checkValue(any());
    }

    @Test
    void should_check_null_value_if_nested_field_key_not_present(){
        ExtensionField nested = mock(ExtensionField.class);
        when(nested.getKey()).thenReturn("not present in holder");
        ValueHolder holder = mock(ValueHolder.class);
        when(holder.get()).thenReturn(ImmutableMap.of("key", "value"));
        MapExtensionField field = MapExtensionField.builder().fields(ListUtil.of(nested)).build();

        field.checkValue(holder);

        verify(nested, times(1)).checkValue(isNull());
    }

    @Test
    void should_check_exact_value_if_nested_field_key_present(){
        ExtensionField nested = mock(ExtensionField.class);
        when(nested.getKey()).thenReturn("present key");
        ValueHolder holder = mock(ValueHolder.class);
        when(holder.get()).thenReturn(ImmutableMap.of("present key", "exact value"));
        MapExtensionField field = MapExtensionField.builder().fields(ListUtil.of(nested)).build();

        field.checkValue(holder);

        verify(nested, times(1)).checkValue(argThat(h-> "exact value".equals(h.get())));
    }
}