package top.abosen.xboot.extensionfield.validator;

import lombok.Value;

import java.util.function.Function;

/**
 * @author qiubaisen
 * @date 2023/2/23
 */
@Value
public
class LengthValidator implements ValueValidator {
    Integer minLength;
    Integer maxLength;
    Function<Object, Integer> lengthFunc;

    @Override
    public boolean valid(Object value) {
        if (value == null) return true;
        if (minLength == null && maxLength == null) return true;
        Integer length = lengthFunc.apply(value);
        return (minLength == null || length >= minLength) && (maxLength == null || length <= maxLength);
    }
}
