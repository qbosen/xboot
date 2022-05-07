package top.abosen.xboot.spring.validator;


import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @author qiubaisen
 * @date 2021/3/31
 */
public class ObjectValidator {
    private static final Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public static <T> void valid(T object, Class<?>... groups) throws ConstraintViolationException {
        Set<ConstraintViolation<T>> constrains = validator.validate(object, groups);
        if (!constrains.isEmpty()) {
            throw new ConstraintViolationException(constrains);
        }
    }
}
