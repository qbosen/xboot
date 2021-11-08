package top.abosen.dddboot.shared.utility.leftbaise;

import javax.annotation.Nullable;
import java.util.function.Function;

/**
 * 用于element之间存在继承关系时的key多态映射
 *
 * @author qiubaisen
 * @date 2021/3/31
 */
public abstract class AbstractContextKey<B extends Context.Element, E extends B> implements Context.Key<E> {
    private final Function<Context.Element, E> safeCast;
    private final Context.Key<?> topmostKey;

    public AbstractContextKey(Context.Key<B> baseKey, Function<Context.Element, E> safeCast) {
        this.safeCast = safeCast;
        this.topmostKey = baseKey instanceof AbstractContextKey ?
                ((AbstractContextKey<?, ?>) baseKey).topmostKey : baseKey;
    }

    @Nullable
    public static <E extends Context.Element> E getPolymorphicElement(Context.Element element, Context.Key<E> key) {
        if (key instanceof AbstractContextKey) {
            AbstractContextKey<?, ?> kk = (AbstractContextKey<?, ?>) key;
            return kk.isSubKey(element.getKey()) ? (E) kk.tryCast(element) : null;
        }
        //noinspection unchecked
        return element.getKey().equals(key) ? (E) element : null;
    }

    public static Context removePolymorphicKey(Context.Element element, Context.Key<?> key) {
        if (key instanceof AbstractContextKey) {
            AbstractContextKey<?, ?> kk = (AbstractContextKey<?, ?>) key;
            return kk.isSubKey(element.getKey()) && kk.tryCast(element) != null ? EmptyContext.INSTANCE : element;
        }
        return element.getKey().equals(key) ? EmptyContext.INSTANCE : element;
    }

    @Nullable
    public E tryCast(Context.Element element) {
        return safeCast.apply(element);
    }

    public boolean isSubKey(Context.Key<?> key) {
        return key.equals(this) || topmostKey.equals(key);
    }
}
