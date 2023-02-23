package top.abosen.xboot.extensionfield;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author qiubaisen
 * @date 2023/2/22
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@type")
public interface ExtensionType {
    List<ExtensionField> getFields();

    default Map<String, ExtensionField> fieldMap(){
        return getFields().stream().collect(Collectors.toMap(ExtensionField::getKey, it -> it, (a, b) -> a));
    }
}
