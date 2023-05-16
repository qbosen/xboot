package top.abosen.xboot.shared.utility.fp;


import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author qiubaisen
 * @since 2020/8/13
 */
public class FluentPropertySetter {

    /**
     * 再从source map中获取对应的值
     *
     * @param source 源map
     * @param idGen  应用于目标的 获取id的方法
     * @param <S>    map value类型
     * @param <D>    目标类型
     * @param <ID>   id类型
     * @return 存储在源map中的值
     */
    public static <S, D, ID> Function<D, S> closureProvider(Map<ID, S> source, Function<D, ID> idGen) {
        return data -> source.get(idGen.apply(data));
    }

    /**
     * 从集合中根据id获取目标值; id重复则使用后一个
     *
     * @param source   源集合
     * @param sourceId 应用于源的 获取id的方法
     * @param destId   应用于目标的 获取id的方法
     * @param <S>      源类型
     * @param <D>      目标类型
     * @param <ID>     id类型
     * @return 一个映射用于获取源中的值
     */
    public static <S, D, ID> Function<D, S> closureProvider(Collection<S> source, Function<S, ID> sourceId, Function<D, ID> destId) {
        return closureProvider(source.stream().collect(Collectors.toMap(sourceId, Function.identity(), (a, b) -> b)), destId);
    }

    /**
     * 关于{@link FluentPropertySetter#closureProvider(Collection, Function, Function)}的lazy形式
     *
     * @param source   源集合的supplier
     * @param sourceId 应用于源的 获取id的方法
     * @param destId   应用于目标的 获取id的方法
     * @param <S>      源类型
     * @param <D>      目标类型
     * @param <ID>     id类型
     * @return 一个映射用于获取源中的值
     */
    public static <S, D, ID> Supplier<Function<D, S>> lazyProvider(Supplier<Collection<S>> source, Function<S, ID> sourceId, Function<D, ID> destId) {
        return () -> closureProvider(source.get(), sourceId, destId);
    }

    /**
     * transfer 属性转移实现
     *
     * @param getter        从源获取属性
     * @param setter        设置属性到目标
     * @param mapper        源属性到目标属性的映射
     * @param prevCondition 前置条件
     * @param postCondition 后置条件
     * @param <S>           源类型
     * @param <D>           目标类型
     * @param <PS>          源属性
     * @param <PD>          目标属性
     * @return 属性转移
     */
    public static <S, D, PS, PD> BiConsumer<S, D> transfer(
            Function<S, PS> getter, BiConsumer<D, PD> setter, Function<PS, PD> mapper,
            BiPredicate<S, D> prevCondition, BiPredicate<D, PD> postCondition) {
        return (source, dest) -> {
            if (prevCondition.test(source, dest)) {
                PD value = mapper.apply(getter.apply(source));
                if (postCondition.test(dest, value)) {
                    setter.accept(dest, value);
                }
            }
        };
    }

    public static <S, D, P> BiConsumer<S, D> transfer(Function<S, P> getter, BiConsumer<D, P> setter) {
        return transfer(getter, setter, Function.identity(), (s, d) -> true, (d, p) -> true);
    }

    public static <S, D, P> BiConsumer<S, D> transfer(Function<S, P> getter, BiConsumer<D, P> setter, Supplier<P> defaultValue) {
        return transfer(getter, setter, s -> s != null ? s : defaultValue.get(), (s, d) -> true, (d, p) -> true);
    }

    @SafeVarargs
    public static <S, D> Function<D, D> propertyTransfer
            (Function<D, S> provider, BiConsumer<S, D>... transfers) {
        return propertyTransfer(() -> provider, transfers);
    }

    /**
     * 组合provider和transfer
     *
     * @param lazyProvider 数据提供者
     * @param transfers    属性转移
     * @param <S>          源类型
     * @param <D>          目标类型
     * @return 已修改的data, 用于链式调用
     */
    @SafeVarargs
    public static <S, D> Function<D, D> propertyTransfer
    (Supplier<Function<D, S>> lazyProvider, BiConsumer<S, D>... transfers) {
        return data -> {
            Arrays.stream(transfers).reduce(BiConsumer::andThen).ifPresent(trans -> trans.accept(lazyProvider.get().apply(data), data));
            return data;
        };
    }
}
