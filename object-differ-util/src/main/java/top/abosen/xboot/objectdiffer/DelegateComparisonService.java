package top.abosen.xboot.objectdiffer;

import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.comparison.ComparisonService;
import de.danielbechler.diff.comparison.ComparisonStrategy;
import de.danielbechler.diff.node.DiffNode;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 代理 {@link ComparisonService}, 实现对包含 {@link DiffEquals} 注解对象的对比功能
 *
 * @author qiubaisen
 * @since 2023/1/15
 */
public class DelegateComparisonService extends ComparisonService {
    public DelegateComparisonService(ObjectDifferBuilder objectDifferBuilder) {
        super(objectDifferBuilder);
    }

    private final Map<Class<?>, ComparisonStrategy> diffEqualsStrategies = new HashMap<>();

    @Override
    public ComparisonStrategy resolveComparisonStrategy(final DiffNode node) {
        return Optional.of(node.getValueType()).map(type ->
                        diffEqualsStrategies.computeIfAbsent(type, key ->
                                diffEqualsMethod(key).orElseGet(() -> diffEqualsField(key).orElse(null))))
                .orElseGet(() -> super.resolveComparisonStrategy(node));
    }

    private static Optional<ComparisonStrategy> diffEqualsMethod(Class<?> valueType) {
        return Arrays.stream(valueType.getDeclaredMethods()).filter(it -> it.isAnnotationPresent(DiffEquals.class))
                .filter(m -> m.getParameterCount() == 0 && m.getReturnType() != Void.TYPE)
                .findFirst().map(EqualsMethodComparisonStrategy::new);
    }

    private static Optional<ComparisonStrategy> diffEqualsField(Class<?> valueType) {
        return Optional.of(Arrays.stream(valueType.getDeclaredFields()).filter(it -> it.isAnnotationPresent(DiffEquals.class)).collect(Collectors.toList()))
                .filter(it -> !it.isEmpty()).map(EqualsFieldComparisonStrategy::new);
    }
    public static class EqualsMethodComparisonStrategy implements ComparisonStrategy {
        final Method method;

        public EqualsMethodComparisonStrategy(Method method) {
            method.setAccessible(true);
            this.method = method;
        }

        @Override
        public void compare(DiffNode node, Class<?> type, Object working, Object base) {
            node.setState(ValueGetter.filterNull(method::invoke).isEquals(working, base) ? DiffNode.State.UNTOUCHED : DiffNode.State.CHANGED);
        }

    }

    public static class EqualsFieldComparisonStrategy implements ComparisonStrategy {

        final List<Field> fields;

        public EqualsFieldComparisonStrategy(List<Field> fields) {
            this.fields = fields;
            fields.forEach(it -> it.setAccessible(true));
        }

        @Override
        public void compare(DiffNode node, Class<?> type, Object working, Object base) {
            boolean allEquals = fields.stream().allMatch(f -> ValueGetter.filterNull(f::get).isEquals(working, base));
            node.setState(allEquals ? DiffNode.State.UNTOUCHED : DiffNode.State.CHANGED);
        }
    }

}
