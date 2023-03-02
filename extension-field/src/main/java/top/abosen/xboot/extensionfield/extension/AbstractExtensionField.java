package top.abosen.xboot.extensionfield.extension;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qiubaisen
 * @date 2023/2/27
 */
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public abstract class AbstractExtensionField implements ExtensionField {

    /**
     * 字段key
     */
    @Getter
    @Setter
    String key;
    @Getter
    @Setter
    String name;

    /**
     * 每个扩展字段都可以有业务上的冗余
     */
    @JsonAnySetter
    @JsonAnyGetter
    @Builder.Default
    Map<String, Object> extension = new HashMap<>();

    @Override
    public Map<String, Object> extension() {
        return extension;
    }
}
