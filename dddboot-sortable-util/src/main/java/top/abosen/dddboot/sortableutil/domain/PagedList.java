package top.abosen.dddboot.sortableutil.domain;

import lombok.Value;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author qiubaisen
 * @date 2021/5/20
 */
@Value
public class PagedList<T> {
    long total;
    List<T> data;

    public <R> PagedList<R> map(Function<T, R> mapper) {
        return new PagedList<>(total, data.stream().map(mapper).collect(Collectors.toList()));
    }

    public static <E> PagedList<E> empty() {
        return new PagedList<>(0, Collections.emptyList());
    }
}
