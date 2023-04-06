package top.abosen.xboot.extensionfield.extension;

import cn.hutool.core.collection.CollUtil;
import com.google.auto.service.AutoService;
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
    protected Optional<String> validMsg() {
        if (CollUtil.isEmpty(fields)) return Optional.of("字段列表不能为空");
        if (fields.stream().map(ExtensionField::getKey).distinct().count() != fields.size())
            return Optional.of("字段key不能重复");

        return fields.stream().map(Validatable::validMessage)
                .filter(Optional::isPresent)
                .map(Optional::get).findFirst();
    }
}
