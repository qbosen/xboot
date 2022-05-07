package top.abosen.xboot.shared.domain;

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
        return getPersistType().isCreated();
    }

    default boolean isModified() {
        return getPersistType().isModified();
    }

    default boolean isDeleted() {
        return getPersistType().isDeleted();
    }

    default boolean isOriginal() {
        return getPersistType().isOriginal();
    }

    enum PersistType {
        ORIGINAL,
        DELETED,
        CREATED,
        MODIFIED;

        public boolean isCreated() {
            return this == PersistType.CREATED;
        }

        public boolean isModified() {
            return this == PersistType.MODIFIED;
        }

        public boolean isDeleted() {
            return this == PersistType.DELETED;
        }

        public boolean isOriginal() {
            return this == PersistType.ORIGINAL;
        }
    }
}
