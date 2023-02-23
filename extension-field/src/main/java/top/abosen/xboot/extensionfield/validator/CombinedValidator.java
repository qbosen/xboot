package top.abosen.xboot.extensionfield.validator;

import lombok.Value;

import java.util.Arrays;
import java.util.List;

/**
 * @author qiubaisen
 * @date 2023/2/23
 */
@Value
public
class CombinedValidator implements ValueValidator {
    List<ValueValidator> validators;

    public CombinedValidator(List<ValueValidator> validators) {
        this.validators = validators;
    }

    public CombinedValidator(ValueValidator... validators) {
        this(Arrays.asList(validators));
    }

    @Override
    public boolean valid(Object value) {
        return validators.stream().allMatch(validator -> validator.valid(value));
    }
}
