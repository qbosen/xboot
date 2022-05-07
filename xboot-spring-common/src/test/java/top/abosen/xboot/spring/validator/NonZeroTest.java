package top.abosen.xboot.spring.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

/**
 * @author qiubaisen
 * @date 2021/5/6
 */

public class NonZeroTest {
    @Test
    public void test_pass() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<ToCheck>> result = validator.validate(new ToCheck());

        // 所有变量是4结尾的 都不通过
        Assertions.assertTrue(
                result.stream().allMatch(it -> it.getPropertyPath().toString().endsWith("4"))
        );
    }

    static class ToCheck {
        @NonZero int i1 = 1;
        @NonZero int i2 = -1;
        @NonZero Integer i3 = null;
        @NonZero int i4 = 0;

        @NonZero short s1 = 1;
        @NonZero short s2 = -1;
        @NonZero Short s3 = null;
        @NonZero short s4 = 0;

        @NonZero long l1 = 1;
        @NonZero long l2 = -1;
        @NonZero Long l3 = null;
        @NonZero long l4 = 0;

        @NonZero float f1 = 1f;
        @NonZero float f2 = -1f;
        @NonZero Float f3 = null;
        @NonZero float f4 = 0f;

        @NonZero double d1 = 1.0;
        @NonZero double d2 = -1.0;
        @NonZero Double d3 = null;
        @NonZero double d4 = 0.0;

        @NonZero byte b1 = 1;
        @NonZero byte b2 = -1;
        @NonZero Byte b3 = null;
        @NonZero byte b4 = 0;

        @NonZero BigInteger bi1 = BigInteger.ONE;
        @NonZero BigInteger bi2 = BigInteger.valueOf(-1);
        @NonZero BigInteger bi3 = null;
        @NonZero BigInteger bi4 = BigInteger.ZERO;

        @NonZero BigDecimal bd1 = BigDecimal.ONE;
        @NonZero BigDecimal bd2 = BigDecimal.valueOf(-1.0);
        @NonZero BigDecimal bd3 = null;
        @NonZero BigDecimal bd4 = BigDecimal.ZERO;
    }

}
