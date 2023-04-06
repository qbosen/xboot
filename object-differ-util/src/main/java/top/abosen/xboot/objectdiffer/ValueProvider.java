package top.abosen.xboot.objectdiffer;

/**
 * @author qiubaisen
 * @since 2023/1/16
 */
public interface ValueProvider {

    String DEFAULT_FORMAT_PROVIDER = "&DEFAULT_PROVIDER&";

    /**
     * 在{@link DiffValue#name()} 中使用
     *
     * @return 注册的实例名称
     */
    default String name() {
        return DEFAULT_FORMAT_PROVIDER;
    }

    /**
     * 同名时可以设置匹配规则
     *
     * @return true 选中执行
     */
    default boolean filter(Class<?> type, Object target) {
        return true;
    }

    Object provide(Class<?> type, Object target);
}
