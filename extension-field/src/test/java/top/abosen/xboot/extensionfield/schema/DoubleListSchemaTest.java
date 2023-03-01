package top.abosen.xboot.extensionfield.schema;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import top.abosen.xboot.extensionfield.valueholder.RefValueHolder;
import top.abosen.xboot.extensionfield.valueholder.ValueHolder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author qiubaisen
 * @date 2023/2/28
 */
class DoubleListSchemaTest {

    @Test
    void should_valid_required() {
        DoubleListSchema schema = DoubleListSchema.builder().required(true).build();
        assertThat(schema.checkValue(null)).isFalse();
        assertThat(schema.checkValue(RefValueHolder.of(null))).isFalse();
    }

    @Test
    void should_check_min_max_value() {
        DoubleListSchema schema = DoubleListSchema.builder().min(1d).max(2d).required(true).build();
        assertThat(schema.checkValue(RefValueHolder.of(Arrays.asList(1, 2, "1.55")))).isTrue();
        assertThat(schema.checkValue(RefValueHolder.of(Arrays.asList(1, 2, 0)))).isFalse();
        assertThat(schema.checkValue(RefValueHolder.of(Arrays.asList(1, 2, "999")))).isFalse();
    }

    @Test
    void should_check_min_max_length() {
        DoubleListSchema schema = DoubleListSchema.builder().minSize(2).maxSize(3).required(true).build();
        assertThat(schema.checkValue(RefValueHolder.of(Arrays.asList(1, 2, 3)))).isTrue();
        assertThat(schema.checkValue(RefValueHolder.of(Arrays.asList(1)))).isFalse();
        assertThat(schema.checkValue(RefValueHolder.of(Arrays.asList(1, 2, "999","0.01")))).isFalse();
    }

    @ParameterizedTest(name = "resolve un-support type: {1}")
    @MethodSource
    void should_resolve_to_empty_list_if_value_not_support(ValueHolder holder, String context) {
        DoubleListSchema schema = new DoubleListSchema();
        schema.resolveValue(holder);
        assertThat(holder.get()).isInstanceOf(List.class)
                .asList().isEmpty();
    }

    static Stream<Arguments> should_resolve_to_empty_list_if_value_not_support() {
        return Stream.of(
                Arguments.of(RefValueHolder.of(1), "integer"),
                Arguments.of(RefValueHolder.of("1.1"), "string"),
                Arguments.of(RefValueHolder.of(3.5), "double")
        );
    }

    @ParameterizedTest(name = "resolve from type: {1}")
    @MethodSource
    void should_resolve_double_list_schema(ValueHolder holder, String context) {
        DoubleListSchema schema = new DoubleListSchema();
        schema.resolveValue(holder);
        assertThat(holder.get()).isInstanceOf(List.class)
                .asList().isNotEmpty()
                .hasOnlyElementsOfType(Double.class);
    }

    static Stream<Arguments> should_resolve_double_list_schema() {
        return Stream.of(
                Arguments.of(RefValueHolder.of(Arrays.asList(1, 2)), "integer list"),
                Arguments.of(RefValueHolder.of(new String[]{"3.14", "-1.0"}), "string array"),
                Arguments.of(RefValueHolder.of(
                        new Number[]{new BigDecimal("3.141519"), 99999L, -123.123}), "number array"),
                Arguments.of(RefValueHolder.of(
                        Arrays.asList(new BigDecimal("3.141519"), 99999L, -123.123, "88.88", 0x123)), "object list")
        );
    }
}