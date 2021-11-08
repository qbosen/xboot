package top.abosen.dddboot.shared.utility.leftbaise;


import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * @author qiubaisen
 * @date 2021/3/31
 */
public final class CombinedContext implements Context {
    private final Context left;
    private final Element element;

    public CombinedContext(Context left, Element element) {
        this.left = left;
        this.element = element;
    }

    @Nullable
    @Override
    public <E extends Element> E get(Key<E> key) {
        CombinedContext cur = this;
        while (true) {
            E e = cur.element.get(key);
            if (e != null) {
                return e;
            }
            Context next = cur.left;
            if (next instanceof CombinedContext) {
                cur = (CombinedContext) next;
            } else {
                return next.get(key);
            }
        }
    }

    @Override
    public <R> R fold(R init, BiFunction<R, Element, R> op) {
        return op.apply(left.fold(init, op), element);
    }

    @Override
    public Context remove(Key<?> key) {
        Element e = element.get(key);
        if (e != null) {
            return left;
        }
        Context newLeft = left.remove(key);
        if (newLeft == left) {
            return this;
        } else if (newLeft == EmptyContext.INSTANCE) {
            return element;
        } else {
            return new CombinedContext(newLeft, element);
        }
    }

    @Override
    public int hashCode() {
        return left.hashCode() + element.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof CombinedContext) {
            CombinedContext context = (CombinedContext) other;
            return context.size() == size() && context.containsAll(this);
        }
        return false;
    }

    private int size() {
        CombinedContext cur = this;
        int size = 2;
        while (true) {
            cur = (CombinedContext) Optional.of(cur).map(CombinedContext::getLeft)
                    .filter(it -> it instanceof CombinedContext).orElse(null);
            if (cur == null) {
                return size;
            }
            size++;
        }
    }

    private boolean contains(Element element) {
        return Objects.equals(get(element.getKey()), element);
    }

    private boolean containsAll(CombinedContext context) {
        CombinedContext cur = context;
        while (true) {
            if (!contains(cur.element)) {
                return false;
            }
            Context next = cur.left;
            if (next instanceof CombinedContext) {
                cur = (CombinedContext) next;
            } else {
                return contains((Element) next);
            }

        }
    }

    @Override
    public String toString() {
        return "[" +
               fold("", (acc, element) -> acc.isEmpty() ? element.toString() : acc + ", " + element.toString())
               + "]";
    }

    public Context getLeft() {
        return left;
    }

    public Element getElement() {
        return element;
    }
}
