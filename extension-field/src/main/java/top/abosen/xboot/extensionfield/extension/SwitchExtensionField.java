package top.abosen.xboot.extensionfield.extension;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.util.StrUtil;
import com.google.auto.service.AutoService;
import lombok.*;
import lombok.experimental.SuperBuilder;
import top.abosen.xboot.extensionfield.validator.Validatable;
import top.abosen.xboot.extensionfield.valueholder.MapValueHolder;
import top.abosen.xboot.extensionfield.valueholder.ValueHolder;

import java.util.Map;
import java.util.Optional;

/**
 * option不重复的field分支
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
public class SwitchExtensionField extends AbstractExtensionField {
    public final String type = "switch";

    Map<String, ExtensionField> options;

    @SuppressWarnings("unchecked")
    @Override
    public boolean checkValue(ValueHolder valueHolder) {
        Object nestedValue = valueHolder.get();
        if (!(nestedValue instanceof Map)) return false;
        Map<String, Object> castValue = (Map<String, Object>) nestedValue;

        String selectOption = castValue.keySet().stream().filter(options::containsKey).findFirst().orElse(null);
        if (selectOption == null) return false;
        if (!options.containsKey(selectOption)) return false;
        return options.get(selectOption).checkValue(MapValueHolder.of(castValue, selectOption));
    }

    @Override
    protected Optional<String> validMsg() {
        if (CollUtil.isEmpty(options)) return Optional.of("字段列表不能为空");
        if(options.keySet().stream().anyMatch(StrUtil::isBlank)) return Optional.of("选项不能为空");
        return options.values().stream().map(Validatable::validMessage)
                .filter(Optional::isPresent)
                .map(Optional::get).findFirst();
    }
}
