package top.abosen.xboot.extensionfield.extension;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import top.abosen.xboot.extensionfield.validator.Validatable;
import top.abosen.xboot.extensionfield.valueholder.ValueHolderChecker;

import java.util.Map;

/**
 * @author qiubaisen
 * @date 2023/2/23
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@type", defaultImpl = SimpleExtensionField.class)
public interface ExtensionField extends ValueHolderChecker, Validatable {
    String getKey();

    Map<String, Object> extension();
}
