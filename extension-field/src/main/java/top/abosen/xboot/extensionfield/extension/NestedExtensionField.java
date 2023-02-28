package top.abosen.xboot.extensionfield.extension;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import com.google.auto.service.AutoService;
import lombok.Getter;
import top.abosen.xboot.extensionfield.validator.Validatable;
import top.abosen.xboot.extensionfield.valueholder.ValueHolder;
import top.abosen.xboot.extensionfield.jackson.JsonSubType;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author qiubaisen
 * @date 2023/2/27
 */
@Getter
@AutoService(ExtensionField.class)
@JsonSubType("nested")
public class NestedExtensionField extends AbstractExtensionField {
    List<ExtensionField> fields;

    @Override
    public boolean checkValue(ValueHolder valueHolder) {
        Object nestedValue = valueHolder.get();
        if (!(nestedValue instanceof Map)) return false;
        return new ExtensionTypeValueMap((Map<String, Object>) nestedValue)
                .valid(() -> fields);
    }

    @Override
    public Optional<String> validMessage() {
        if(CollUtil.isEmpty(fields)) return Optional.of("嵌套字段不能为空");
        return fields.stream().map(Validatable::validMessage)
                .filter(Optional::isPresent)
                .map(Optional::get).findFirst();
    }
}
