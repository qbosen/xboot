package top.abosen.xboot.shared.utility.fp;

/**
 * @author qiubaisen
 * @since 2021/3/31
 */
@FunctionalInterface
public interface SupplierWithException<T, E extends Exception> {
    T query() throws E;
}
