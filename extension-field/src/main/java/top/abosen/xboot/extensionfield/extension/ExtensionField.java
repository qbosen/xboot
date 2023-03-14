package top.abosen.xboot.extensionfield.extension;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import top.abosen.xboot.extensionfield.validator.Validatable;
import top.abosen.xboot.extensionfield.valueholder.ValueHolderChecker;

import java.util.Map;

/**
 * @author qiubaisen
 * @date 2023/2/23
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, defaultImpl = SimpleExtensionField.class)
@Schema(oneOf = {SimpleExtensionField.class, MapExtensionField.class, ListExtensionField.class, SwitchExtensionField.class})
public interface ExtensionField extends ValueHolderChecker, Validatable {
    @JsonIgnore
    String getType();
    String getKey();

    String getName();

    Map<String, Object> extension();
}
