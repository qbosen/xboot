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
public class FormatSource {

    private final Map<String, LinkedList<FormatProvider>> holder = new HashMap<>();

    public FormatSource registerInstance(FormatProvider provider) {
        holder.compute(provider.name(), (k, v) -> v == null ? new LinkedList<>() : v).addFirst(provider);
        return this;
    }

    public Object provideValue(DiffField.Format format, Class<?> type, Object target) {
        return pickProvider(format, type, target).provide(type, target);
    }

    private FormatProvider instanceProvider(String source, Class<?> type, Object target) {
        return Optional.ofNullable(holder.getOrDefault(source, holder.get(FormatProvider.DEFAULT_FORMAT_PROVIDER)))
                .flatMap(providers -> providers.stream().filter(it -> it.filter(type, target)).findFirst())
                .orElse(TO_STRING);
    }


    protected static final FormatProvider TO_STRING = (type, target) -> Optional.ofNullable(target).map(String::valueOf).orElse(null);


    private FormatProvider errorOr(FormatProvider provider, FormatProvider defaultProvider) {
        return (type, target) -> {
            try {
                return provider.provide(type, target);
            } catch (Exception e) {
                log.error("FormatProvider调用失败", e);
                return defaultProvider.provide(type, target);
            }
        };
    }

    private FormatProvider pickProvider(DiffField.Format format, Class<?> type, Object target) {
        if (format == null) return TO_STRING;
        switch (format.source()) {
            case METHOD:
                return errorOr(methodProvider(format), TO_STRING);
            case INSTANCE:
                return errorOr(instanceProvider(format.instanceSource(), type, target), TO_STRING);
            default:
                return TO_STRING;
        }
    }

    private static FormatProvider methodProvider(DiffField.Format format) {
        return (type, target) -> access(type, target, format.methodSource());
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

interface FormatProvider {

    String DEFAULT_FORMAT_PROVIDER = "&DEFAULT_PROVIDER&";

    /**
     * 在{@link DiffField.Format#name()} 中使用
     *
     * @return 注册的实例名称
     */
    default String name() {
        return DEFAULT_FORMAT_PROVIDER;
    }

    /**
     * 同名时可以设置匹配规则
     *
     * @return true 选中执行
     */
    default boolean filter(Class<?> type, Object target) {
        return true;
    }

    Object provide(Class<?> type, Object target);
}
