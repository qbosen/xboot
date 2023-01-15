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
    private final Class<? extends DiffFieldParser> functionClass;
    private final DiffNode node;
    private final Object rootSource;
    private final Object rootTarget;

    Difference(DiffNode node, Object source, Object target) {
        DiffField diffField = Optional.ofNullable(node.getPropertyAnnotation(DiffField.class))
                .orElseGet(() -> node.getFieldAnnotation(DiffField.class));
        this.node = node;
        propertyName = node.getPropertyName();
        displayName = Optional.ofNullable(diffField).map(DiffField::name)
                .filter(s -> s.length() != 0)
                .orElse(propertyName);
        rootSource = source;
        rootTarget = target;
        valueType = node.getValueType() == null ? Object.class : node.getValueType();
        sourceValue = node.canonicalGet(rootSource);
        targetValue = node.canonicalGet(rootTarget);
        functionClass = diffField == null ? DiffFieldParser.NONE.class : diffField.function();
    }


}
