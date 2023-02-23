package top.abosen.xboot.extensionfield;

import java.util.Map;
import java.util.Optional;

/**
 * @author qiubaisen
 * @date 2023/2/22
 */
public interface ExtensionTypeValue {
    Object value();

    Map<String, Object> toMap();

    boolean valid(ExtensionType type);

    Optional<Object> query(ExtensionField field);
}
