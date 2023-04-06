package top.abosen.xboot.shared.utility.leftbaise;

import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * @author qiubaisen
 * @since 2021/3/31
 */
public interface Context {
    @Nullable
    <E extends Element> E get(Key<E> key);

    <R> R fold(R init, BiFunction<R, Element, R> op);

    default void foreach(Consumer<Element> op) {
        this.fold(null, (acc, element) -> {
            op.accept(element);
            return acc;
        });
    }

    Context remove(Key<?> key);

    default Context add(Context context) {
        if (context == EmptyContext.INSTANCE) {
            return this;
        }
        return context.fold(this, (acc, element) -> {
            // 如果新context的key与旧的相同，就删掉旧的
            Context remove = acc.remove(element.getKey());
            if (remove == EmptyContext.INSTANCE) {
                return element;
            }
            return new CombinedContext(remove, element);
        });
    }

    interface Key<E extends Element> {
    }

    interface Element extends Context {
        Key<?> getKey();

        @Override
        @Nullable
        default <E extends Element> E get(Key<E> key) {
            if (getKey().equals(key)) {
                //noinspection unchecked
                return (E) this;
            } else {
                return null;
            }
        }

        @Override
        default <R> R fold(R init, BiFunction<R, Element, R> op) {
            return op.apply(init, this);
        }

        @Override
        default Context remove(Key<?> key) {
            if (getKey().equals(key)) {
                return EmptyContext.INSTANCE;
            } else {
                return this;
            }
        }
    }
}
