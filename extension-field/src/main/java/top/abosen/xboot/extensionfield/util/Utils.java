package top.abosen.xboot.extensionfield.util;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.stream.StreamUtil;
import lombok.experimental.UtilityClass;
import top.abosen.xboot.extensionfield.valueholder.ListValueHolder;
import top.abosen.xboot.extensionfield.valueholder.ValueHolder;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author qiubaisen
 * @date 2023/2/22
 */

@UtilityClass
public class Utils {
    public static <T extends Annotation> Optional<T> getAnno(Class<?> clazz, Class<T> anno) {
        return Opt.of(clazz).map(it -> it.getAnnotation(anno))
                .or(() -> Opt.of(1).flattedMap(x ->
                        Arrays.stream(clazz.getAnnotations()).map(it -> it.annotationType().getAnnotation(anno))
                                .filter(Objects::nonNull).findFirst()))
                .toOptional();
    }

    /**
     * 将 holder 的值转换为list,并返回对应的holder
     */
    public static List<ValueHolder> listValue(ValueHolder holder) {
        if (holder == null || holder.get() == null) return Collections.emptyList();

        final List<Object> data;
        Object value = holder.get();
        if (value.getClass().isArray()) {
            data = Arrays.stream((Object[]) value).collect(Collectors.toList());
        } else if (value instanceof Iterable) {
            data = StreamUtil.of(((Iterable<?>) value)).collect(Collectors.toList());
        } else {
            data = new ArrayList<>();
        }
        holder.set(data);

        return IntStream.range(0, data.size()).mapToObj(i -> ListValueHolder.of(data, i)).collect(Collectors.toList());
    }
}
