package top.abosen.xboot.extensionfield.schema;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import top.abosen.xboot.extensionfield.validator.LengthValidator;
import top.abosen.xboot.extensionfield.validator.NumberValidator;
import top.abosen.xboot.extensionfield.validator.ValueValidator;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author qiubaisen
 * @date 2023/2/23
 */
class ValueValidatorTest {

    @Nested
    class LengthTest {

        private ValueValidator validator;

        @BeforeEach
        void setup() {
            validator = new LengthValidator(1, 5, o -> (int) o);
        }

        @Test
        void should_return_true_for_null_value() {
            assertThat(validator.valid(null)).isTrue();
        }

        @Test
        void should_valid_length() {
            assertThat(validator.valid(0)).isFalse();
            assertThat(validator.valid(6)).isFalse();
            assertThat(validator.valid(5)).isTrue();
        }

    }

    @Nested
    class NumberTest {
        private NumberValidator<Integer> validator;

        @BeforeEach
        void setup() {
            validator = new NumberValidator<>(1, 5);
        }

        @Test
        void should_valid_number() {
            assertThat(validator.valid(null)).isTrue();
            assertThat(validator.valid("not support")).isTrue();

            assertThat(validator.valid(-1.0)).isFalse();
            assertThat(validator.valid(9L)).isFalse();
            assertThat(validator.valid(3.2)).isTrue();
        }
    }
}