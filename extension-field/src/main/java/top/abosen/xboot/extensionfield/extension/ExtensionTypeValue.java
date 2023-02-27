package top.abosen.xboot.extensionfield.extension;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Map;

/**
 * @author qiubaisen
 * @date 2023/2/22
 */
@JsonSerialize(as = ExtensionTypeValueMap.class)
@JsonDeserialize(as = ExtensionTypeValueMap.class)
public interface ExtensionTypeValue {

    Map<String, Object> toMap();

    boolean valid(ExtensionType type);

}

