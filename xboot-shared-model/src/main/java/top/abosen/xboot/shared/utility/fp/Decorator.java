package top.abosen.xboot.shared.utility.fp;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 对同一个类型数据进行修正/装饰
 *
 * @author qiubaisen
 * @date 2021/5/28
 */

@FunctionalInterface
public interface Decorator<T> {
    /**
     * 不做任何处理
     *
     * @param <U> 类型
     * @return 空装饰
     */
    static <U> Decorator<U> nil() {
        return it -> it;
    }

    /**
     * 把一个 consumer 转化为一个装饰
     *
     * @param consumer 消费操作
     * @param <U>      数据类型
     * @return 完成消费操作的装饰
     */
    static <U> Decorator<U> from(Consumer<U> consumer) {
        return it -> {
            consumer.accept(it);
            return it;
        };
    }

    /**
     * 对R中的U进行装饰
     *
     * @param getter    从R获取U的映射
     * @param setter    把U设置回R
     * @param decorator 对U的装饰
     * @param <R>       外部类型
     * @param <U>       内部类型
     * @return R的装饰
     */
    static <R, U> Decorator<R> compose(Function<R, U> getter, BiConsumer<R, U> setter, Decorator<U> decorator) {
        return from(t -> setter.accept(t, decorator.decorate(getter.apply(t))));
    }

    /**
     * 执行装饰
     *
     * @param t 被装饰的数据
     * @return 装饰后的结果
     */
    T decorate(T t);

    /**
     * 组合装饰
     *
     * @param next 下一步装饰
     * @return 组合结果
     */
    default Decorator<T> next(Decorator<T> next) {
        return t -> next.decorate(this.decorate(t));
    }

}
