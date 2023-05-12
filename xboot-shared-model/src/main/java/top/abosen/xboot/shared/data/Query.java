package top.abosen.xboot.shared.data;

/**
 * @author qiubaisen
 * @since 2021/3/31
 */
public interface Query extends Pageable {
    Sort getSort();

    void setSort(Sort sort);

    /**
     * 查询对象是否无效, 用于提前返回空结果,避免数据库查询
     *
     * @return 是否无效
     */
    default boolean invalid() {
        return false;
    }
}
