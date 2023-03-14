package top.abosen.xboot.extensionfield.validator;

import java.util.Optional;

/**
 * @author qiubaisen
 * @date 2023/2/27
 */
public interface Validatable {
    default Optional<String> validMessage() {
        return Optional.empty();
    }
}
