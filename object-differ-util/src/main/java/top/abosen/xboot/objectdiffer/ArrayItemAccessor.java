package top.abosen.xboot.objectdiffer;

import de.danielbechler.diff.access.Accessor;
import de.danielbechler.diff.access.TypeAwareAccessor;
import de.danielbechler.diff.identity.EqualsIdentityStrategy;
import de.danielbechler.diff.identity.IdentityStrategy;
import de.danielbechler.diff.selector.CollectionItemElementSelector;
import de.danielbechler.diff.selector.ElementSelector;


public class ArrayItemAccessor implements TypeAwareAccessor, Accessor {

    private final Object referenceItem;
    private final IdentityStrategy identityStrategy;

    public ArrayItemAccessor(final Object referenceItem) {
        this(referenceItem, EqualsIdentityStrategy.getInstance());
    }

    public ArrayItemAccessor(final Object referenceItem,
                             final IdentityStrategy identityStrategy) {
        this.referenceItem = referenceItem;
        this.identityStrategy = identityStrategy;
    }

    @Override
    public Class<?> getType() {
        return referenceItem != null ? referenceItem.getClass() : null;
    }

    @Override
    public String toString() {
        return "array item " + getElementSelector();
    }

    /**
     * {@link ElementSelector} 的目的是在NodePath中提供 equals,用于匹配路径, array和collection可以共用一个
     */
    @Override
    public ElementSelector getElementSelector() {
        final CollectionItemElementSelector selector = new CollectionItemElementSelector(referenceItem);
        return identityStrategy == null ? selector : selector.copyWithIdentityStrategy(identityStrategy);
    }

    @Override
    public Object get(final Object target) {
        Object[] targetArray = objectAsArray(target);
        if (targetArray == null) {
            return null;
        }
        for (final Object item : targetArray) {
            if (item != null && identityStrategy.equals(item, referenceItem)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public void set(final Object target, final Object value) {
        throw new UnsupportedOperationException("Cannot modify array item");
    }

    private static Object[] objectAsArray(final Object object) {
        if (object == null) {
            return null;
        } else if (object.getClass().isArray()) {
            return (Object[]) object;
        }
        throw new IllegalArgumentException(object.getClass().toString());
    }

    @Override
    public void unset(final Object target) {
        throw new UnsupportedOperationException("Cannot modify array item");
    }
}
