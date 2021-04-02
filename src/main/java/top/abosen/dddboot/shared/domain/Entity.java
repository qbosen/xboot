package top.abosen.dddboot.shared.domain;

/**
 * @author qiubaisen
 * @date 2021/3/31
 */
public interface Entity<ID> {
    ID getId();

    void setId(ID id);
}
