package top.abosen.xboot.extensionfield.extension;

import cn.hutool.core.collection.CollUtil;
import com.google.auto.service.AutoService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import top.abosen.xboot.extensionfield.jackson.JsonSubType;
import top.abosen.xboot.extensionfield.validator.Validatable;
import top.abosen.xboot.extensionfield.valueholder.MapValueHolder;
import top.abosen.xboot.extensionfield.valueholder.ValueHolder;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * @author qiubaisen
 * @date 2023/2/27
 */
@Getter
@Setter
@AutoService(ExtensionField.class)
@JsonSubType("nested")
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class NestedExtensionField extends AbstractExtensionField {

    @ArraySchema(schema = @Schema(oneOf = {SimpleExtensionField.class, NestedExtensionField.class}))
    List<ExtensionField> fields;

    @SuppressWarnings("unchecked")
    @Override
    public boolean checkValue(ValueHolder valueHolder) {
        Object nestedValue = valueHolder.get();
        if (!(nestedValue instanceof List)) return false;
        // todo refactor
        if (!((List<?>) nestedValue).stream().allMatch(Map.class::isInstance)) return false;
        List<Map<String, Object>> castNestedValue = (List<Map<String, Object>>) nestedValue;

        if (CollUtil.size(fields) != CollUtil.size(castNestedValue)) return false;
        if (!IntStream.range(0, fields.size()).allMatch(i -> castNestedValue.get(i).containsKey(fields.get(i).getKey())))
            return false;

        return IntStream.range(0, fields.size()).allMatch(i -> fields.get(i).checkValue(MapValueHolder.of(castNestedValue.get(i), fields.get(i).getKey())));
    }

    @Override
    public Optional<String> validMessage() {
        if (CollUtil.isEmpty(fields)) return Optional.of("嵌套字段不能为空");
        return fields.stream().map(Validatable::validMessage)
                .filter(Optional::isPresent)
                .map(Optional::get).findFirst();
    }
}
