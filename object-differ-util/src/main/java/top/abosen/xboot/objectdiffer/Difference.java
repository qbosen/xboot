package top.abosen.xboot.objectdiffer;

import de.danielbechler.diff.node.DiffNode;
import lombok.Getter;

import java.util.Collection;
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
    private final DiffValue valueFormat;


    Difference(DiffNode node, Object source, Object target) {
        Diff diff = Annotations.getNodeAnno(node, Diff.class);
        this.node = node;
        valueType = node.getValueType() == null ? Object.class : node.getValueType();
        // 获取属性名称;获取最近层级的属性名称;类型名称(root节点,且root可比较)
        propertyName = Optional.ofNullable(node.getPropertyName()).orElseGet(valueType::getSimpleName);
        displayName = Optional.ofNullable(diff).map(Diff::displayName)
                .filter(s -> s.length() != 0)
                .orElse(propertyName);
        rootSource = source;
        rootTarget = target;
        sourceValue = node.canonicalGet(rootSource);
        targetValue = node.canonicalGet(rootTarget);

        valueFormat = Optional.ofNullable(diff).map(Diff::format)
                .filter(it -> it.source().handle())
                .orElse(null);
    }


    /**
     * 存在特定的 diffValue表达, 就不用深入对比子节点了
     *
     * @return 是不是子节点
     */
    public boolean isEndPoint() {
        return !node.hasChildren() || (valueFormat != null && valueFormat.source().handle());
    }

    public boolean isContainer() {
        return valueType != null && (Collection.class.isAssignableFrom(valueType) || valueType.isArray());
    }

    /**
     * @return 是否为一个 不同端点
     */
    public boolean isDifferent() {
        return node.hasChanges() && isEndPoint();
    }
}
