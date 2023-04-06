package top.abosen.xboot.extensionfield.extension;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author qiubaisen
 * @since 2023/2/27
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

    @Override
    public final Optional<String> validMessage() {
        if(StrUtil.isBlank(key)) return Optional.of("字段key不能为空");
        return validMsg();
    }

    protected abstract Optional<String> validMsg();
}
