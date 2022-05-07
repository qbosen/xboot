package top.abosen.xboot.shared.utility;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author qiubaisen
 * @date 2021/3/31
 */
public abstract class CollectionHelper {
    public static <T> T random(Collection<T> collection) {
        if (collection.isEmpty()) {
            throw new NoSuchElementException("Collection is empty");
        }
        return elementAt(collection, ThreadLocalRandom.current().nextInt(collection.size()));
    }

    public static <T> T elementAt(Iterable<T> iterable, int index) {
        if (iterable instanceof List) {
            return ((List<T>) iterable).get(index);
        }
        return elementAtOrElse(iterable, index, x -> {
            throw new IndexOutOfBoundsException("Collection doesn't contain element at index " + index);
        });
    }

    public static <T> T elementAtOrElse(Iterable<T> iterable, int index, Function<Integer, T> defaultValue) {
        if (iterable instanceof List) {
            return getOrElse((List<T>) iterable, index, defaultValue);
        }
        if (index < 0) {
            return defaultValue.apply(index);
        }
        Iterator<T> iterator = iterable.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            T element = iterator.next();
            if (index == count++) {
                return element;
            }
        }
        return defaultValue.apply(index);
    }

    public static <T> T getOrElse(List<T> list, int index, Function<Integer, T> defaultValue) {
        return index >= 0 && index < list.size() ? list.get(index) : defaultValue.apply(index);
    }

    public static <T> boolean notEmpty(Collection<T> collection) {
        return collection != null && !collection.isEmpty();
    }

    public static <T, O> List<T> keepOrder(List<O> order, Collection<T> data, Function<T, O> orderMapping) {
        ListMultimap<O, T> multiMap = MultimapBuilder.hashKeys().arrayListValues().build();
        data.forEach(d -> multiMap.put(orderMapping.apply(d), d));
        return order.stream().flatMap(o -> multiMap.get(o).stream()).collect(Collectors.toList());
    }
}
