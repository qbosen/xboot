package top.abosen.xboot.extensionfield.util;

import lombok.experimental.UtilityClass;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author qiubaisen
 * @date 2023/3/2
 */

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@UtilityClass
public class Opt8 {
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