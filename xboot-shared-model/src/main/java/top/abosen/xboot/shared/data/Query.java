package top.abosen.xboot.shared.data;

/**
 * @author qiubaisen
 * @since 2021/3/31
 */
public interface Query extends Pageable {
    Sort getSort();

    void setSort(Sort sort);
}
