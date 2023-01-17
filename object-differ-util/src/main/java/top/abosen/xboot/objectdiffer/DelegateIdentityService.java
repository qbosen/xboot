package top.abosen.xboot.objectdiffer;

import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.identity.IdentityService;
import de.danielbechler.diff.identity.IdentityStrategy;
import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.util.Exceptions;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author qiubaisen
 * @date 2023/1/17
 */
public class DelegateIdentityService extends IdentityService {
    public DelegateIdentityService(ObjectDifferBuilder objectDifferBuilder) {
        super(objectDifferBuilder);
    }

    private final Map<Class<?>, IdentityStrategy> diffIdentityStrategies = new HashMap<>();

    @Override
    public IdentityStrategy resolveIdentityStrategy(DiffNode node) {
        return Optional.of(node.getValueType()).map(type ->
                        diffIdentityStrategies.computeIfAbsent(type, key ->
                                diffIdentityMethod(key).orElseGet(() -> diffIdentityField(key).orElse(null))))
                .orElseGet(() -> super.resolveIdentityStrategy(node));
    }

    private static Optional<IdentityStrategy> diffIdentityMethod(Class<?> valueType) {
        return Arrays.stream(valueType.getDeclaredMethods()).filter(it -> it.isAnnotationPresent(DiffEquals.class))
                .filter(m -> m.getParameterCount() == 0 && m.getReturnType() != Void.TYPE)
                .findFirst().map(IdentityMethodIdentityStrategy::new);
    }

    private static Optional<IdentityStrategy> diffIdentityField(Class<?> valueType) {
        return Optional.of(Arrays.stream(valueType.getDeclaredFields()).filter(it -> it.isAnnotationPresent(DiffEquals.class)).collect(Collectors.toList()))
                .filter(it -> !it.isEmpty()).map(IdentityFieldIdentityStrategy::new);
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

    private interface ValueGetter {
        Object getValue(Object target) throws Exception;

    }

    public static class IdentityMethodIdentityStrategy implements IdentityStrategy {
        final Method method;

        public IdentityMethodIdentityStrategy(Method method) {
            method.setAccessible(true);
            this.method = method;
        }

        @Override
        public boolean equals(Object working, Object base) {
            return isEquals(working, base, method::invoke);
        }
    }

    public static class IdentityFieldIdentityStrategy implements IdentityStrategy {

        final List<Field> fields;

        public IdentityFieldIdentityStrategy(List<Field> fields) {
            this.fields = fields;
            fields.forEach(it -> it.setAccessible(true));
        }

        @Override
        public boolean equals(Object working, Object base) {
            return fields.stream().allMatch(f -> isEquals(working, base, f::get));
        }
    }
}
