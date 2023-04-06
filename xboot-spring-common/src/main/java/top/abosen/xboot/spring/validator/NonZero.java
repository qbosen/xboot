package top.abosen.xboot.spring.validator;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiubaisen
 * @since 2021/5/6
 */

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {
        NonZero.NotZeroIntegerValidator.class,
        NonZero.NotZeroLongValidator.class,
        NonZero.NotZeroShortValidator.class,
        NonZero.NotZeroFloatValidator.class,
        NonZero.NotZeroDoubleValidator.class,
        NonZero.NotZeroByteValidator.class,
        NonZero.NotZeroBigIntegerValidator.class,
        NonZero.NotZeroBigDecimalValidator.class,
})
public @interface NonZero {
    String message() default "must not zero";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class NotZeroIntegerValidator implements ConstraintValidator<NonZero, Integer> {
        @Override
        public boolean isValid(Integer num, ConstraintValidatorContext constraintValidatorContext) {
            return num == null || NumberSignHelper.signum(num) != 0;
        }
    }

    class NotZeroLongValidator implements ConstraintValidator<NonZero, Long> {
        @Override
        public boolean isValid(Long num, ConstraintValidatorContext constraintValidatorContext) {
            return num == null || NumberSignHelper.signum(num) != 0;
        }
    }

    class NotZeroShortValidator implements ConstraintValidator<NonZero, Short> {
        @Override
        public boolean isValid(Short num, ConstraintValidatorContext constraintValidatorContext) {
            return num == null || NumberSignHelper.signum(num) != 0;
        }
    }

    class NotZeroFloatValidator implements ConstraintValidator<NonZero, Float> {
        @Override
        public boolean isValid(Float num, ConstraintValidatorContext constraintValidatorContext) {
            return num == null || NumberSignHelper.signum(num) != 0;
        }
    }

    class NotZeroDoubleValidator implements ConstraintValidator<NonZero, Double> {
        @Override
        public boolean isValid(Double num, ConstraintValidatorContext constraintValidatorContext) {
            return num == null || NumberSignHelper.signum(num) != 0;
        }
    }

    class NotZeroByteValidator implements ConstraintValidator<NonZero, Byte> {
        @Override
        public boolean isValid(Byte num, ConstraintValidatorContext constraintValidatorContext) {
            return num == null || NumberSignHelper.signum(num) != 0;
        }
    }


    class NotZeroBigIntegerValidator implements ConstraintValidator<NonZero, BigInteger> {
        @Override
        public boolean isValid(BigInteger num, ConstraintValidatorContext constraintValidatorContext) {
            return num == null || NumberSignHelper.signum(num) != 0;
        }
    }

    class NotZeroBigDecimalValidator implements ConstraintValidator<NonZero, BigDecimal> {
        @Override
        public boolean isValid(BigDecimal num, ConstraintValidatorContext constraintValidatorContext) {
            return num == null || NumberSignHelper.signum(num) != 0;
        }
    }
}
