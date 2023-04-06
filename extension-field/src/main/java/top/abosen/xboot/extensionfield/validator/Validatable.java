package top.abosen.xboot.extensionfield.validator;

import java.util.Optional;

/**
 * @author qiubaisen
 * @since 2023/2/27
 */
public interface Validatable {
    default Optional<String> validMessage() {
        return Optional.empty();
    }
}
