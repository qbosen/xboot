package top.abosen.xboot.extensionfield.validator;

import lombok.Value;

/**
 * @author qiubaisen
 * @since 2023/2/23
 */
@Value
public
class NumberValidator<T extends Number> implements ValueValidator {
    T min;
    T max;

    @Override
    public boolean valid(Object value) {
        if (value == null) return true;
        if (min == null && max == null) return true;
        if (!(value instanceof Number)) return true;

        double number = ((Number) value).doubleValue();
        return (min == null || min.doubleValue() <= number) && (max == null || max.doubleValue() >= number);
    }
}
