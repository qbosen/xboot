package top.abosen.xboot.extensionfield.extension;

import cn.hutool.core.collection.CollUtil;
import top.abosen.xboot.extensionfield.validator.Validatable;
import top.abosen.xboot.extensionfield.valueholder.MapValueHolder;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author qiubaisen
 * @since 2023/2/22
 */

public interface ExtensionType extends Validatable {
    List<ExtensionField> getFields();

    default Map<String, ExtensionField> fieldMap() {
        return getFields().stream().collect(Collectors.toMap(ExtensionField::getKey, it -> it, (a, b) -> a));
    }

    @Override
    default Optional<String> validMessage() {
        if (CollUtil.isEmpty(getFields())) return Optional.of("扩展类型字段不能为空");
        if (getFields().stream().map(ExtensionField::getKey).distinct().count() != getFields().size()) {
            return Optional.of("扩展类型字段key不能重复");
        }

        return getFields().stream().map(Validatable::validMessage)
                .filter(Optional::isPresent).map(Optional::get).findFirst();
    }

    default boolean valid(Map<String, Object> map) {
        return getFields().stream().allMatch(field -> field.checkValue(
                map.containsKey(field.getKey()) ? MapValueHolder.of(map, field.getKey()) : null)
        );
    }

    default void update(Map<String, Object> map){
        getFields().forEach(field -> field.updateValue(
                map.containsKey(field.getKey()) ? MapValueHolder.of(map, field.getKey()) : null)
        );
    }
}
