package top.abosen.dddboot.sortableutil.domain;

/**
 * @author qiubaisen
 * @date 2021/4/30
 */
public class Flag {
    private FlagEnum flagEnum = FlagEnum.ORIGINAL;

    public void create() {
        flagEnum = FlagEnum.CREATED;
    }

    public void modify() {
        flagEnum = FlagEnum.MODIFIED;
    }

    public void delete() {
        flagEnum = FlagEnum.DELETED;
    }

    public void notModify() {
        flagEnum = FlagEnum.ORIGINAL;
    }

    // region delegates

    public boolean isCreated() {
        return flagEnum.isCreated();
    }

    public boolean isModified() {
        return flagEnum.isModified();
    }

    public boolean isDeleted() {
        return flagEnum.isDeleted();
    }

    public boolean isOriginal() {
        return flagEnum.isOriginal();
    }

    // endregion

    private enum FlagEnum {
        ORIGINAL,
        DELETED,
        CREATED,
        MODIFIED;

        boolean isCreated() {
            return this == CREATED;
        }

        boolean isModified() {
            return this == MODIFIED;
        }

        boolean isDeleted() {
            return this == DELETED;
        }

        boolean isOriginal() {
            return this == ORIGINAL;
        }
    }
}
