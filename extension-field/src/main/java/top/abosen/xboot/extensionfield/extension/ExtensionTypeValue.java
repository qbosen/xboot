package top.abosen.xboot.extensionfield.extension;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

/**
 * @author qiubaisen
 * @since 2023/2/22
 */
@JsonSerialize(as = ExtensionTypeValueMap.class)
@JsonDeserialize(as = ExtensionTypeValueMap.class)
@Schema(implementation = Map.class)
public interface ExtensionTypeValue {

    Map<String, Object> toMap();

    boolean valid(ExtensionType type);

}

