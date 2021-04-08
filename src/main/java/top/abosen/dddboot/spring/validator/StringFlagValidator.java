package top.abosen.dddboot.spring.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author qiubaisen
 * @date 2021/3/31
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = FlagValidatorClass.class)
public @interface StringFlagValidator {
    String[] value() default {};

    boolean nullable() default true;

    String message() default "flag is not found";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
