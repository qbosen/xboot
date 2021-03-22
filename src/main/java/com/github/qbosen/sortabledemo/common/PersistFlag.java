package com.github.qbosen.sortabledemo.common;

/**
 * @author qiubaisen
 * @date 2021/3/22
 */

public interface PersistFlag {
    PersistType getPersistType();

    void setPersistType(PersistType persistType);

    default void create() {
        setPersistType(PersistType.CREATED);
    }

    default void modify() {
        setPersistType(PersistType.MODIFIED);
    }

    default void delete() {
        setPersistType(PersistType.DELETED);
    }

    default void notModify() {
        setPersistType(PersistType.ORIGINAL);
    }

    default boolean isCreated() {
        return persistType() == PersistType.CREATED;
    }

    default boolean isModified() {
        return persistType() == PersistType.MODIFIED;
    }

    default boolean isDeleted() {
        return persistType() == PersistType.DELETED;
    }

    default boolean isOriginal() {
        return persistType() == PersistType.ORIGINAL;
    }

    enum PersistType {
        ORIGINAL,
        DELETED,
        CREATED,
        MODIFIED
    }

    default PersistType persistType(){
        if(getPersistType() == null){
            notModify();
        }
        return getPersistType();
    }
}
