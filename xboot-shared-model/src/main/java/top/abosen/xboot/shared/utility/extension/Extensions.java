package top.abosen.xboot.shared.utility.extension;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author qiubaisen
 * @since 2023/3/10
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class Extensions {
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
        return filter(value, action, null);
    }
    public static <T> T filter(T value, Predicate<T> action, T defaultValue) {
        Objects.requireNonNull(action);
        if (action.test(value)) {
            return value;
        }
        return defaultValue;
    }

    public static <T> boolean isEmpty(Optional<T> optional) {
        return !optional.isPresent();
    }

    public static <T> void ifPresentOrElse(Optional<T> optional, Consumer<? super T> action, Runnable emptyAction) {
        if (optional.isPresent()) {
            action.accept(optional.get());
        } else {
            emptyAction.run();
        }
    }

    public static <T> Optional<T> or(Optional<T> optional, Supplier<? extends Optional<? extends T>> supplier) {
        Objects.requireNonNull(supplier);
        if (optional.isPresent()) {
            return optional;
        } else {
            @SuppressWarnings("unchecked")
            Optional<T> r = (Optional<T>) supplier.get();
            return Objects.requireNonNull(r);
        }
    }

    public static <T> Stream<T> stream(Optional<T> optional) {
        return optional.map(Stream::of).orElseGet(Stream::empty);
    }

    public static <T> T orElseThrow(Optional<T> optional) {
        if (!optional.isPresent()) {
            throw new NoSuchElementException("No value present");
        }
        return optional.get();
    }

    public static <T> Optional<T> peek(Optional<T> optional, Consumer<T> action) {
        Objects.requireNonNull(action);
        if (!optional.isPresent()) return Optional.empty();
        action.accept(optional.get());
        return optional;
    }

}
