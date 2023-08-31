package top.abosen.xboot.extensionfield.extension;

import cn.hutool.core.collection.CollUtil;
import com.google.auto.service.AutoService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import top.abosen.xboot.extensionfield.validator.Validatable;
import top.abosen.xboot.extensionfield.valueholder.MapValueHolder;
import top.abosen.xboot.extensionfield.valueholder.ValueHolder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * key不重复的field组合
 *
 * @author qiubaisen
 * @since 2023/2/27
 */
@Getter
@Setter
@AutoService(ExtensionField.class)
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "组合扩展字段; 当前字段是确定的多个扩展字段的组合;", example = "{\"@type\":\"map\",\"key\":\"address\",\"name\":\"地址\",\"fields\":[{\"@type\":\"simple\",\"key\":\"province\",\"name\":\"省份\",\"schema\":{\"@type\":\"string\",\"required\":true,\"default_value\":null,\"min_length\":1,\"max_length\":20,\"regex\":null},\"widget\":{\"@type\":\"input\",\"name\":\"输入省份\",\"style\":null,\"multiple\":false}},{\"@type\":\"simple\",\"key\":\"city\",\"name\":\"城市\",\"schema\":{\"@type\":\"string\",\"required\":true,\"default_value\":null,\"min_length\":1,\"max_length\":20,\"regex\":null},\"widget\":{\"@type\":\"input\",\"name\":\"输入城市\",\"style\":null,\"multiple\":false}},{\"@type\":\"simple\",\"key\":\"street\",\"name\":\"街道\",\"schema\":{\"@type\":\"string\",\"required\":true,\"default_value\":null,\"min_length\":1,\"max_length\":20,\"regex\":null},\"widget\":{\"@type\":\"input\",\"name\":\"输入街道\",\"style\":null,\"multiple\":false}},{\"@type\":\"simple\",\"key\":\"code\",\"name\":\"邮编\",\"schema\":{\"@type\":\"integer\",\"required\":true,\"default_value\":100000,\"min\":0,\"max\":999999},\"widget\":{\"@type\":\"input\",\"name\":\"输入邮编\",\"style\":null,\"multiple\":false}}],\"desc\":\"填写地址, 组合了多个元素\"}")
public class MapExtensionField extends AbstractExtensionField {
    public final String type = "map";

    List<ExtensionField> fields;

    @SuppressWarnings("unchecked")
    @Override
    public boolean checkValue(ValueHolder valueHolder) {
        Object nestedValue = valueHolder.get();
        if (!(nestedValue instanceof Map)) return false;
        Map<String, Object> castValue = (Map<String, Object>) nestedValue;
        return fields.stream().allMatch(f -> f.checkValue(castValue.containsKey(f.getKey()) ? MapValueHolder.of(castValue, f.getKey()) : null));
    }

    @Override
    public void updateValue(ValueHolder valueHolder) {
        Object nestedValue = valueHolder.get();
        if (!(nestedValue instanceof Map)) return;
        Map<String, Object> castValue = (Map<String, Object>) nestedValue;
        fields.forEach(f -> f.updateValue(castValue.containsKey(f.getKey()) ? MapValueHolder.of(castValue, f.getKey()) : null));
    }

    @Override
    protected Optional<String> validMsg() {
        if (CollUtil.isEmpty(fields)) return Optional.of("字段列表不能为空");
        if (fields.stream().map(ExtensionField::getKey).distinct().count() != fields.size())
            return Optional.of("字段key不能重复");

        return fields.stream().map(Validatable::validMessage)
                .filter(Optional::isPresent)
                .map(Optional::get).findFirst();
    }
}
