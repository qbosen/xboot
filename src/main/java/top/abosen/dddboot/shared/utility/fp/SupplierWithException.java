package top.abosen.dddboot.shared.utility.fp;

/**
 * @author qiubaisen
 * @date 2021/3/31
 */
@FunctionalInterface
public interface SupplierWithException<T, E extends Exception> {
    T query() throws E;
}
