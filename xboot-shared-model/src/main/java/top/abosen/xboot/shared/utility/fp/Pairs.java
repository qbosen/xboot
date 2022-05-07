package top.abosen.xboot.shared.utility.fp;


import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public abstract class Pairs {

    public static <K, V> Pair<K, V> fromEntry(Map.Entry<K, V> entry) {
        return Pair.of(entry.getKey(), entry.getValue());
    }

    public static <K, V, A, B> Function<Map.Entry<A, B>, Pair<K, V>> fromEntry(
            Function<? super A, ? extends K> kf, Function<? super B, ? extends V> vf) {
        return entry -> Pair.of(kf.apply(entry.getKey()), vf.apply(entry.getValue()));
    }

    public static <A, B> Collector<Pair<A, B>, ?, Map<A, B>> toMap() {
        return Collectors.toMap(Pair::getLeft, Pair::getRight);
    }

    public static <T, K, V> Function<T, Pair<K, V>> toPair(
            Function<? super T, ? extends K> kf, Function<? super T, ? extends V> vf) {
        return t -> Pair.of(kf.apply(t), vf.apply(t));
    }

    public static <K, V, A, B> Function<Pair<A, B>, Pair<K, V>> mapping(
            Function<? super A, ? extends K> kf, Function<? super B, ? extends V> vf) {
        return pair -> Pair.of(kf.apply(pair.getLeft()), vf.apply(pair.getRight()));
    }

    public static <V, A, B> Function<Pair<A, B>, Pair<A, V>> valueMapping(Function<? super B, ? extends V> map) {
        return pair -> Pair.of(pair.getLeft(), map.apply(pair.getRight()));
    }

    public static <V, A, B> Function<Pair<A, B>, Pair<A, V>> mappingValue(Function<Pair<A, B>, ? extends V> map) {
        return pair -> Pair.of(pair.getLeft(), map.apply(pair));
    }

}
