package top.abosen.xboot.objectdiffer;

import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.comparison.ComparisonService;
import de.danielbechler.diff.comparison.ComparisonStrategy;
import de.danielbechler.diff.node.DiffNode;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 代理 {@link ComparisonService}, 实现对包含 {@link DiffField.Id} 注解对象的对比功能
 *
 * @author qiubaisen
 * @date 2023/1/15
 */
public class CustomComparisonService extends ComparisonService {
    public CustomComparisonService(ObjectDifferBuilder objectDifferBuilder) {
        super(objectDifferBuilder);
    }

    private final Map<Class<?>, ComparisonStrategy> diffFieldIdStrategies = new HashMap<>();

    @Override
    public ComparisonStrategy resolveComparisonStrategy(final DiffNode node) {
        final Class<?> valueType = node.getValueType();
        if (valueType != null) {
            ComparisonStrategy diffFieldIdStrategy = diffFieldIdStrategies.computeIfAbsent(valueType, x -> {
                List<Field> diffFieldIds = Arrays.stream(valueType.getDeclaredFields()).filter(it -> it.isAnnotationPresent(DiffField.Id.class)).collect(Collectors.toList());
                if (diffFieldIds.isEmpty()) {
                    return null;
                } else {
                    return new DiffFieldIdAnnotationComparisonStrategy(diffFieldIds);
                }
            });

            if (diffFieldIdStrategy != null) return diffFieldIdStrategy;
        }

        return super.resolveComparisonStrategy(node);
    }


    @Slf4j
    public static class DiffFieldIdAnnotationComparisonStrategy implements ComparisonStrategy {

        final List<Field> fields;

        public DiffFieldIdAnnotationComparisonStrategy(List<Field> fields) {
            this.fields = fields;
            fields.forEach(it -> it.setAccessible(true));
        }

        @Override
        public void compare(DiffNode node, Class<?> type, Object working, Object base) {
            boolean allEquals = fields.stream().allMatch(f -> {
                try {
                    return Objects.equals(f.get(working), f.get(base));
                } catch (IllegalAccessException e) {
                    log.error("访问字段值失败:", e);
                    throw new RuntimeException(e);
                }
            });

            node.setState(allEquals ? DiffNode.State.UNTOUCHED : DiffNode.State.CHANGED);
        }
    }

}
