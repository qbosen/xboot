package top.abosen.xboot.extensionfield.schema;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import top.abosen.xboot.extensionfield.valueholder.RefValueHolder;
import top.abosen.xboot.extensionfield.valueholder.ValueHolder;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author qiubaisen
 * @date 2023/2/28
 */
class DoubleSchemaTest {

    @Test
    void should_valid_required() {
        Schema schema = DoubleSchema.of(null, null, true, null);
        assertThat(schema.checkValue(null)).isFalse();
        assertThat(schema.checkValue(RefValueHolder.of(null))).isFalse();
        assertThat(schema.checkValue(RefValueHolder.of(1.0))).isTrue();
    }

    @Test
    void should_set_default_value() {
        Schema schema = DoubleSchema.of(null, null, true, 9.9);
        ValueHolder holder = RefValueHolder.of(null);
        assertThat(schema.checkValue(holder)).isTrue();
        assertThat(holder.get()).isEqualTo(9.9);
    }

    @Test
    void should_check_min_max() {
        Schema schema = DoubleSchema.of(1d, 2d, true, 9.9);
        assertThat(schema.checkValue(RefValueHolder.of(1d))).isTrue();
        assertThat(schema.checkValue(RefValueHolder.of(2d))).isTrue();
        assertThat(schema.checkValue(RefValueHolder.of(1.5555))).isTrue();
        assertThat(schema.checkValue(RefValueHolder.of(0))).isFalse();
        assertThat(schema.checkValue(RefValueHolder.of(3))).isFalse();
    }

    @Test
    void should_check_min_max_for_default_value() {
        Schema schema = DoubleSchema.of(1d, 2d, true, 9.9);
        ValueHolder holder = RefValueHolder.of(null);
        assertThat(schema.checkValue(holder)).isFalse();
        assertThat(holder.get()).isEqualTo(9.9);

        holder = RefValueHolder.of(null);
        schema = DoubleSchema.of(1d, 2d, true, 1.5);
        assertThat(schema.checkValue(holder)).isTrue();
        assertThat(holder.get()).isEqualTo(1.5);
    }

    @ParameterizedTest(name = "resolve from type: {1}")
    @MethodSource
    void should_resolve_double_schema(ValueHolder holder, String context) {
        Schema schema = new DoubleSchema();
        schema.resolveValue(holder);

        assertThat(holder.get()).isNotNull()
                .isInstanceOf(Double.class);
    }

    static Stream<Arguments> should_resolve_double_schema() {
        return Stream.of(
                Arguments.of(RefValueHolder.of("3.13"), "string"),
                Arguments.of(RefValueHolder.of(123), "int"),
                Arguments.of(RefValueHolder.of(3.14), "double"),
                Arguments.of(RefValueHolder.of(999L), "long"),
                Arguments.of(RefValueHolder.of(new BigDecimal("3.1415926")), "big decimal")
        );
    }
}