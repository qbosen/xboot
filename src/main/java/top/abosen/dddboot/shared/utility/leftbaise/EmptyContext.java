package top.abosen.dddboot.shared.utility.leftbaise;

import javax.annotation.Nullable;
import java.util.function.BiFunction;

/**
 * @author qiubaisen
 * @date 2021/3/31
 */
public final class EmptyContext implements Context {
    public final static EmptyContext INSTANCE = new EmptyContext();

    private EmptyContext() {
    }

    @Nullable
    @Override
    public <E extends Element> E get(Key<E> key) {
        return null;
    }

    @Override
    public <R> R fold(R init, BiFunction<R, Element, R> op) {
        return init;
    }

    @Override
    public Context remove(Key<?> key) {
        return this;
    }

    @Override
    public Context add(Context context) {
        return context;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override public boolean equals(Object obj) {
        return this == obj;
    }

    @Override
    public String toString() {
        return "EmptyContext";
    }
}