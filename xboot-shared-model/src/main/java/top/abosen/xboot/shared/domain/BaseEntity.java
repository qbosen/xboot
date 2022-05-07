package top.abosen.xboot.shared.domain;

/**
 * @author qiubaisen
 * @date 2021/3/31
 */
public abstract class BaseEntity<ID> implements Entity<ID> {
    @Override
    public boolean equals(Object entity) {
        if (this.getId() == null || entity == null) {
            return false;
        }

        if (!(entity instanceof BaseEntity)) {
            return false;
        }

        return this.getId().equals(((BaseEntity<?>) entity).getId());
    }

    @Override
    public int hashCode() {
        return this.getId() == null ? 0 : this.getId().hashCode();
    }

}
