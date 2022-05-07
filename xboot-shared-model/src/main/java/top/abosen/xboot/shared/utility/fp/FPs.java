package top.abosen.xboot.shared.utility.fp;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author qiubaisen
 * @date 2021/3/31
 */
public abstract class FPs {

    public static <T, R> R tryOrDefault(SupplierWithException<T, ?> query, Function<T, R> mapper, R def, Consumer<Exception> handler) {
        try {
            return Optional.ofNullable(query.query()).map(mapper).orElse(def);
        } catch (Exception e) {
            if (handler != null) {
                handler.accept(e);
            }
            return def;
        }
    }
}
