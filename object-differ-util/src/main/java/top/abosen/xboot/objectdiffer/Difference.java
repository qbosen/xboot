package top.abosen.xboot.objectdiffer;

import de.danielbechler.diff.node.DiffNode;
import lombok.Getter;

import java.util.Optional;

/**
 * @author qiubaisen
 * @date 2023/1/14
 */
@Getter
public class Difference {
    private final String displayName;
    private final String propertyName;
    private final Class<?> valueType;
    private final Object sourceValue;
    private final Object targetValue;
    private final DiffNode node;
    private final Object rootSource;
    private final Object rootTarget;
    private final DiffField.Format valueFormat;


    Difference(DiffNode node, Object source, Object target) {
        DiffField diffField = Optional.ofNullable(node.getPropertyAnnotation(DiffField.class))
                .orElseGet(() -> node.getFieldAnnotation(DiffField.class));
        this.node = node;
        valueType = node.getValueType() == null ? Object.class : node.getValueType();
        // 获取属性名称;获取最近层级的属性名称;类型名称(root节点,且root可比较)
        propertyName = Optional.ofNullable(node.getPropertyName()).orElseGet(valueType::getSimpleName);
        displayName = Optional.ofNullable(diffField).map(DiffField::name)
                .filter(s -> s.length() != 0)
                .orElse(propertyName);
        rootSource = source;
        rootTarget = target;
        sourceValue = node.canonicalGet(rootSource);
        targetValue = node.canonicalGet(rootTarget);

        // 先从字段注解获取, 再从类上获取
        valueFormat = Optional.ofNullable(diffField).map(DiffField::format)
                .filter(it -> it.source().handle())
                .orElseGet(() -> Optional.ofNullable(node.getValueType())
                        .map(type -> type.getAnnotation(DiffField.Format.class))
                        .orElse(null));
    }

}
