package top.abosen.xboot.extensionfield.extension;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import top.abosen.xboot.extensionfield.validator.ValueValidator;
import top.abosen.xboot.extensionfield.valueholder.ValueHolder;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author qiubaisen
 * @date 2023/3/2
 */
class ListExtensionFieldTest {
    ExtensionField target;
    ListExtensionField field;
    ValueValidator validator;


    @BeforeEach
    void setup() {
        target = mock(ExtensionField.class);
        validator = mock(ValueValidator.class);
        field = ListExtensionField.builder().key("key").target(target).build();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "\t\n"})
    @NullSource
    void should_valid_false_if_field_key_is_empty(String key) {
        ListExtensionField field = ListExtensionField.builder().key(key).build();

        assertThat(field.validMessage()).isPresent().hasValue("字段key不能为空");
    }

    @Test
    void should_valid_false_if_target_field_is_null() {
        ListExtensionField field = ListExtensionField.builder().key("key").target(null).build();

        assertThat(field.validMessage()).isPresent().hasValue("目标字段不能为空");
    }

    @Test
    void should_valid_target_field_and_validator() {
        ListExtensionField spy = spy(field);
        when(spy.buildValidator()).thenReturn(validator);
        when(target.validMessage()).thenReturn(Optional.empty());
        when(validator.validMessage()).thenReturn(Optional.of("validator result"));

        assertThat(spy.validMessage()).isPresent().hasValue("validator result");
        verify(target, times(1)).validMessage();
        verify(validator, times(1)).validMessage();
    }

    @Test
    void should_valid_target_field_and_ignore_validator_if_target_invalid() {
        ListExtensionField spy = spy(field);
        when(spy.buildValidator()).thenReturn(validator);
        when(target.validMessage()).thenReturn(Optional.of("target invalid"));
        when(validator.validMessage()).thenReturn(Optional.empty());

        assertThat(spy.validMessage()).isPresent().hasValue("target invalid");
        verify(target, times(1)).validMessage();
        verify(validator, times(0)).validMessage();
    }

    static Stream<Object> should_return_false_if_check_value_type_not_list() {
        return Stream.of(1, "str", ImmutableMap.of("k", "v"), new long[]{1L, 2L});
    }

    @ParameterizedTest
    @MethodSource
    @NullSource
    void should_return_false_if_check_value_type_not_list(Object value) {
        ValueHolder holder = mock(ValueHolder.class);
        when(holder.get()).thenReturn(value);

        ListExtensionField field = ListExtensionField.builder().build();

        assertThat(field.checkValue(holder)).isFalse();
    }


    @Test
    void should_check_value_via_validator_and_target_field() {
        ValueHolder holder = mock(ValueHolder.class);
        ListExtensionField spy = spy(field);
        when(spy.buildValidator()).thenReturn(validator);
        when(validator.valid(any())).thenReturn(true);
        when(holder.get()).thenReturn(Arrays.asList(1, 2, 3));
        when(target.checkValue(any())).thenReturn(true);

        assertThat(spy.checkValue(holder)).isTrue();
        // 每个参数都会被target校验
        verify(target).checkValue(argThat(h -> Objects.equals(h.get(), 1)));
        verify(target).checkValue(argThat(h -> Objects.equals(h.get(), 2)));
        verify(target).checkValue(argThat(h -> Objects.equals(h.get(), 3)));
        verify(validator, times(1)).valid(eq(Arrays.asList(1, 2, 3)));
    }

}