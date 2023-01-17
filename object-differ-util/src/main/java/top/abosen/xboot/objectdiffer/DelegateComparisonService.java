package top.abosen.xboot.objectdiffer;

import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.comparison.ComparisonService;
import de.danielbechler.diff.comparison.ComparisonStrategy;
import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.util.Exceptions;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 代理 {@link ComparisonService}, 实现对包含 {@link DiffEquals} 注解对象的对比功能
 *
 * @author qiubaisen
 * @date 2023/1/15
 */
public class DelegateComparisonService extends ComparisonService {
    public DelegateComparisonService(ObjectDifferBuilder objectDifferBuilder) {
        super(objectDifferBuilder);
    }

    private final Map<Class<?>, ComparisonStrategy> diffFieldIdStrategies = new HashMap<>();

    @Override
    public ComparisonStrategy resolveComparisonStrategy(final DiffNode node) {
        return Optional.of(node.getValueType()).map(type ->
                        diffFieldIdStrategies.computeIfAbsent(type, key ->
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

    private static boolean isEquals(Object working, Object base, ValueGetter valueGetter) {
        try {
            if (working == null && base == null) return true;
            else if (working == null || base == null) return false;
            else {
                Object workingValue = valueGetter.getValue(working);
                Object baseValue = valueGetter.getValue(base);
                return Objects.equals(workingValue, baseValue);
            }
        } catch (Exception e) {
            throw Exceptions.escalate(e);
        }
    }
    interface ValueGetter {
        Object getValue(Object target) throws Exception;

    }
    public static class EqualsMethodComparisonStrategy implements ComparisonStrategy {
        final Method method;

        public EqualsMethodComparisonStrategy(Method method) {
            method.setAccessible(true);
            this.method = method;
        }

        @Override
        public void compare(DiffNode node, Class<?> type, Object working, Object base) {
            node.setState(isEquals(working, base, method::invoke) ? DiffNode.State.UNTOUCHED : DiffNode.State.CHANGED);

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
            boolean allEquals = fields.stream().allMatch(f -> isEquals(working, base, f::get));
            node.setState(allEquals ? DiffNode.State.UNTOUCHED : DiffNode.State.CHANGED);
        }
    }

}
