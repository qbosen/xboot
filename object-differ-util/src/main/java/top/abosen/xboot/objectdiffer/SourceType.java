package top.abosen.xboot.objectdiffer;

/**
 * 对象值在输出对比结果时的来源
 *
 * @author qiubaisen
 * @since 2023/1/16
 */
public enum SourceType {
    /**
     * 值来自于指定方法, 方法名由{@link DiffValue#methodName()}指定, 可以是实例方法或静态方法
     */
    METHOD,
    /**
     * 由 {@link ValueProvider#provide(Class, Object)} 提供值,
     * 需要提前在 {@link ObjectDiffer.Builder#registerProvider(ValueProvider)} 注册;
     * 在 {@link DiffValue#providerName()} 中指定
     */
    PROVIDER,
    /**
     * 非{@code IGNORE}的对象被视为一个整体参与比较
     */
    IGNORE;


    public boolean handle() {
        return this != IGNORE;
    }
}
