package top.abosen.xboot.objectdiffer;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;


/**
 * @author qiubaisen
 * @date 2023/1/16
 */

@Slf4j
public class ValueProviders {

    private final Map<String, LinkedList<ValueProvider>> holder = new HashMap<>();

    public ValueProviders register(ValueProvider provider) {
        holder.compute(provider.name(), (k, v) -> v == null ? new LinkedList<>() : v).addFirst(provider);
        return this;
    }

    public Object provideValue(DiffValue format, Class<?> type, Object target) {
        return pickProvider(format, type, target).provide(type, target);
    }

    private ValueProvider byProvider(String source, Class<?> type, Object target) {
        return Optional.ofNullable(holder.getOrDefault(source, holder.get(ValueProvider.DEFAULT_FORMAT_PROVIDER)))
                .flatMap(providers -> providers.stream().filter(it -> it.filter(type, target)).findFirst())
                .orElse(TO_STRING);
    }


    protected static final ValueProvider TO_STRING = (type, target) -> Optional.ofNullable(target).map(String::valueOf).orElse(null);


    private ValueProvider errorOr(ValueProvider provider, ValueProvider defaultProvider) {
        return (type, target) -> {
            try {
                return provider.provide(type, target);
            } catch (Exception e) {
                log.error("FormatProvider调用失败", e);
                return defaultProvider.provide(type, target);
            }
        };
    }

    private ValueProvider pickProvider(DiffValue format, Class<?> type, Object target) {
        if (format == null) return TO_STRING;
        switch (format.source()) {
            case METHOD:
                return errorOr(byMethod(format), TO_STRING);
            case PROVIDER:
                return errorOr(byProvider(format.providerName(), type, target), TO_STRING);
            default:
                return TO_STRING;
        }
    }

    private static ValueProvider byMethod(DiffValue format) {
        return (type, target) -> access(type, target, format.methodName());
    }


    protected static Object access(Object target, String methodName) {
        if (target == null) {
            return null;
        }
        return access(target.getClass(), target, methodName);
    }

    protected static Object access(Class<?> type, Object target, String methodName) {
        try {
            final Method method = type.getMethod(methodName);
            method.setAccessible(true);
            if (target == null && !Modifier.isStatic(method.getModifiers())) {
                throw new RuntimeException("target must not be null when invoke instance method");
            }
            return method.invoke(target);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


}

