package top.abosen.xboot.spring.validator;

import com.google.common.collect.Sets;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

/**
 * @author qiubaisen
 * @since 2021/3/31
 */
public class StringFlagValidatorClass implements ConstraintValidator<StringFlagValidator, String> {
    private Set<String> valuesSet;
    private boolean nullable;

    @Override
    public void initialize(StringFlagValidator flagValidator) {
        this.valuesSet = Sets.newHashSet(flagValidator.value());
        this.nullable = flagValidator.nullable();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            //当状态为空时使用默认值
            return nullable;
        }

        return valuesSet.contains(value);
    }
}