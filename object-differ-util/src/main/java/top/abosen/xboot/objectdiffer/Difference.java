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

    private final DiffNode node;
    private final Object rootSource;
    private final Object rootTarget;


    @Getter(lazy = true)
    private final Diff diff = diffAnno(node);
    @Getter(lazy = true)
    private final Class<?> valueType = valueType(node);
    @Getter(lazy = true)
    private final String propertyName = propertyName(node);
    @Getter(lazy = true)
    private final String displayName = displayName();
    @Getter(lazy = true)
    private final Object sourceValue = sourceValue(node);
    @Getter(lazy = true)
    private final Object targetValue = targetValue(node);
    @Getter(lazy = true)
    private final DiffValue valueFormat = valueFormat();


    Difference(DiffNode node, Object source, Object target) {
        this.node = node;
        this.rootSource = source;
        this.rootTarget = target;
    }

    private DiffValue valueFormat() {
        return Optional.ofNullable(getDiff()).map(Diff::format)
                .filter(it -> it.source().handle())
                .orElse(null);
    }

    private Object targetValue(DiffNode node) {
        return node.canonicalGet(rootTarget);
    }

    private Object sourceValue(DiffNode node) {
        return node.canonicalGet(getRootSource());
    }

    private String displayName() {
        return Optional.ofNullable(getDiff()).map(Diff::displayName)
                .filter(s -> s.length() != 0)
                .orElseGet(this::getPropertyName);
    }

    private String propertyName(DiffNode node) {
        return Optional.ofNullable(node.getPropertyName()).orElseGet(() -> getValueType().getSimpleName());
    }

    private static Class<?> valueType(DiffNode node) {
        return node.getValueType() == null ? Object.class : node.getValueType();
    }

    private static Diff diffAnno(DiffNode node) {
        return Annotations.getNodeAnno(node, Diff.class);
    }


    /**
     * 存在特定的 diffValue表达, 就不用深入对比子节点了
     *
     * @return 是不是子节点
     */
    public boolean isEndPoint() {
        return !getNode().hasChildren() || (getValueFormat() != null && getValueFormat().source().handle());
    }

    public boolean isContainer() {
        return getValueType() != null && (Collection.class.isAssignableFrom(getValueType()) || getValueType().isArray());
    }

    /**
     * @return 是否为一个 不同端点
     */
    public boolean isDifferent() {
        return getNode().hasChanges() && isEndPoint();
    }
}
