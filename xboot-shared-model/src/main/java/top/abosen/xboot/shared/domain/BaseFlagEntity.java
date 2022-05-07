package top.abosen.xboot.shared.domain;

import java.io.Serializable;

/**
 * @author qiubaisen
 * @date 2021/3/31
 */
public abstract class BaseFlagEntity<ID extends Serializable> extends BaseEntity<ID> implements PersistFlag {
    private transient PersistType persistType;

    protected BaseFlagEntity() {
        notModify();
    }


    @Override
    public PersistType getPersistType() {
        return this.persistType;
    }

    @Override
    public void persistType(PersistType persistType) {
        this.persistType = persistType == null ? PersistType.ORIGINAL : persistType;
    }
}
