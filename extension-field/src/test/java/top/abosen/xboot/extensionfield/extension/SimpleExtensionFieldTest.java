package top.abosen.xboot.extensionfield.extension;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import top.abosen.xboot.extensionfield.schema.Schema;
import top.abosen.xboot.extensionfield.valueholder.ValueHolder;
import top.abosen.xboot.extensionfield.widget.Widget;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author qiubaisen
 * @date 2023/3/2
 */
class SimpleExtensionFieldTest {

    private Schema schema;
    private Widget widget;
    private SimpleExtensionField field;

    @BeforeEach
    void setup() {
        schema = mock(Schema.class);
        widget = mock(Widget.class);
        field = SimpleExtensionField.builder().key("key").schema(schema).widget(widget).build();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "\t\n"})
    @NullSource
    void should_valid_false_if_field_key_is_empty(String key) {
        SimpleExtensionField field = SimpleExtensionField.builder().key(key).build();

        assertThat(field.validMessage()).isPresent().hasValue("字段key不能为空");
    }

    @Test
    void should_valid_false_if_schema_is_null() {
        SimpleExtensionField field = SimpleExtensionField.builder().key("key").schema(null).widget(mock(Widget.class)).build();

        assertThat(field.validMessage()).isPresent().hasValue("值和组件不能为空");
    }

    @Test
    void should_valid_false_if_widget_is_null() {
        SimpleExtensionField field = SimpleExtensionField.builder().key("key").schema(mock(Schema.class)).widget(null).build();

        assertThat(field.validMessage()).isPresent().hasValue("值和组件不能为空");
    }

    @Test
    void should_valid_schema_and_return_message() {
        when(schema.validMessage()).thenReturn(Optional.of("schema"));

        assertThat(field.validMessage()).isPresent().hasValue("schema");

        verify(schema, times(1)).validMessage();
        verify(widget, times(0)).validMessage();

    }

    @Test
    void should_valid_widget_and_return_message_if_schema_valid_pass() {
        when(schema.validMessage()).thenReturn(Optional.empty());
        when(widget.validMessage()).thenReturn(Optional.of("widget"));

        assertThat(field.validMessage()).isPresent().hasValue("widget");

        verify(schema, times(1)).validMessage();
        verify(widget, times(1)).validMessage();
    }


    @Test
    void should_valid_schema_widget_return_empty_if_valid() {
        when(schema.validMessage()).thenReturn(Optional.empty());
        when(widget.validMessage()).thenReturn(Optional.empty());

        assertThat(field.validMessage()).isEmpty();

        verify(schema, times(1)).validMessage();
        verify(widget, times(1)).validMessage();
    }


    @Test
    void should_check_value_via_schema_and_widget() {
        ValueHolder holder = mock(ValueHolder.class);
        when(schema.checkValue(same(holder))).thenReturn(true);
        when(widget.checkValue(same(holder))).thenReturn(true);

        assertThat(field.checkValue(holder)).isTrue();

        verify(schema, times(1)).checkValue(same(holder));
        verify(widget, times(1)).checkValue(same(holder));
    }
}