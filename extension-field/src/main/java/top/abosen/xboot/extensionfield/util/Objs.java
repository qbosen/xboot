package top.abosen.xboot.extensionfield.util;

import lombok.experimental.UtilityClass;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author qiubaisen
 * @since 2023/3/2
 */
@UtilityClass
public class Objs {
    public static <T> Optional<T> opt(T value) {
        return Optional.ofNullable(value);
    }

    public static <T, R> R let(T value, Function<T, R> action) {
        Objects.requireNonNull(action);
        return action.apply(value);
    }

    public static <T> T also(T value, Consumer<T> action) {
        Objects.requireNonNull(action);
        action.accept(value);
        return value;
    }

    public static <T> T filter(T value, Predicate<T> action) {
        Objects.requireNonNull(action);
        if (action.test(value)) {
            return value;
        }
        return null;
    }

}
