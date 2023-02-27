package top.abosen.xboot.extensionfield.extension;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author qiubaisen
 * @date 2023/2/27
 */
public abstract class AbstractExtensionField implements ExtensionField {
    /**
     * 字段key
     */
    @Getter
    @Setter
    String key;

    /**
     * 每个扩展字段都可以有业务上的冗余
     */
    @JsonAnySetter
    @JsonAnyGetter
    Map<String, Object> extension;
}
