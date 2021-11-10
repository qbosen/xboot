package top.abosen.dddboot.shared.data;

/**
 * @author qiubaisen
 * @date 2021/3/31
 */
public interface Query extends Pageable {
    Sort getSort();

    void setSort(Sort sort);
}
