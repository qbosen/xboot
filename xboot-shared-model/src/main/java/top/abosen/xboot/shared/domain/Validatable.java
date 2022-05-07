package top.abosen.xboot.shared.domain;

/**
 * @author qiubaisen
 * @date 2021/3/31
 */
public interface Validatable {
    void validate(Class<?>... groups);
}
