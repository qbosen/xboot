package top.abosen.xboot.spring.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author qiubaisen
 * @date 2021/3/31
 */
public class FlagValidatorClass implements ConstraintValidator<FlagValidator, Integer> {
    private int[] values;

    @Override
    public void initialize(FlagValidator flagValidator) {
        this.values = flagValidator.value();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            //当状态为空时使用默认值
            return true;
        }
        for (int expect : values) {
            if (expect == value) {
                return true;
            }
        }
        return false;
    }
}