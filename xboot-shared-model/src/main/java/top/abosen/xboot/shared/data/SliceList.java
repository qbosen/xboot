package top.abosen.xboot.shared.data;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author qiubaisen
 * @date 2021/3/31
 */
public interface SliceList<T> extends Iterable<T> {

    List<T> getElements();

    int getSize();

    int getTotalPages();

    long getTotalSize();

    @Nonnull
    @Override
    default Iterator<T> iterator() {
        return this.getElements().iterator();
    }

    <R> SliceList<R> of(List<R> elements, long totalSize);

    default <R> SliceList<R> map(Function<T, R> mapper) {
        return of(
                getElements().stream().map(mapper).collect(Collectors.toList()),
                getTotalSize()
        );
    }


    default <R> SliceList<R> map(Function<T, R> mapper, Predicate<T> beforeFilter, Predicate<R> afterFilter) {
        return of(
                getElements().stream().filter(beforeFilter).map(mapper).filter(afterFilter).collect(Collectors.toList()),
                getTotalSize()
        );
    }

    <R> SliceList<R> elements(List<R> elements);

    <R> SliceList<R> cast();
}