package top.abosen.xboot.extensionfield.util;

import cn.hutool.core.lang.Opt;
import lombok.experimental.UtilityClass;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

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
}
