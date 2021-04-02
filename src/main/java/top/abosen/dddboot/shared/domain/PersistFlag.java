package top.abosen.dddboot.shared.domain;

/**
 * @author qiubaisen
 * @date 2021/3/31
 */
public interface PersistFlag {
    PersistType getPersistType();

    void persistType(PersistType persistType);

    default void create() {
        persistType(PersistType.CREATED);
    }

    default void modify() {
        persistType(PersistType.MODIFIED);
    }

    default void delete() {
        persistType(PersistType.DELETED);
    }

    default void notModify() {
        persistType(PersistType.ORIGINAL);
    }

    default boolean isCreated() {
        return getPersistType() == PersistType.CREATED;
    }

    default boolean isModified() {
        return getPersistType() == PersistType.MODIFIED;
    }

    default boolean isDeleted() {
        return getPersistType() == PersistType.DELETED;
    }

    default boolean isOriginal() {
        return getPersistType() == PersistType.ORIGINAL;
    }

    enum PersistType {
        ORIGINAL,
        DELETED,
        CREATED,
        MODIFIED
    }
}
