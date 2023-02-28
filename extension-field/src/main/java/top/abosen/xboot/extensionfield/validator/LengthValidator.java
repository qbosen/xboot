package top.abosen.xboot.extensionfield.validator;

import cn.hutool.core.collection.CollUtil;
import lombok.Value;

import java.util.Optional;
import java.util.function.Function;

/**
 * @author qiubaisen
 * @date 2023/2/23
 */
@Value
public class LengthValidator implements ValueValidator {
    Integer minLength;
    Integer maxLength;
    Function<Object, Integer> lengthFunc;

    public static LengthValidator collection(Integer minLength, Integer maxLength) {
        return new LengthValidator(minLength, maxLength, CollUtil::size);
    }

    public static ValueValidator string(Integer minLength, Integer maxLength) {
        return new LengthValidator(minLength, maxLength, (value) -> Optional.ofNullable(value)
                .map(it -> String.valueOf(it).length()).orElse(0));
    }

    @Override
    public boolean valid(Object value) {
        if (value == null) return true;
        if (minLength == null && maxLength == null) return true;
        Integer length = lengthFunc.apply(value);
        if (length == null) return true;
        return (minLength == null || length >= minLength) && (maxLength == null || length <= maxLength);
    }

}
