package top.abosen.xboot.extensionfield.validator;

import lombok.Value;

import java.util.Objects;

/**
 * @author qiubaisen
 * @since 2023/2/23
 */
@Value
public
class NonnullValidator implements ValueValidator {
    boolean required;

    @Override
    public boolean valid(Object value) {
        return !required || Objects.nonNull(value);
    }
}
