package top.abosen.xboot.shared.data.support;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import top.abosen.xboot.shared.data.Query;
import top.abosen.xboot.shared.data.Sort;

import java.util.Objects;

/**
 * @author qiubaisen
 * @since 2021/3/31
 */

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public abstract class QuerySupport extends PageableSupport implements Query {
    private Sort sort;

    protected QuerySupport() {
    }

    @Override
    public Sort getSort() {
        if (Objects.isNull(this.sort)) {
            sort = new DefaultSort();
        }
        return this.sort;
    }

    @Override
    public void setSort(Sort sort) {
        if (Objects.isNull(sort)) {
            this.sort = new DefaultSort();
        } else {
            this.sort = sort;
        }
    }

    @Override
    public boolean invalid() {
        return getPage() <= 0 || getSize() <= 0;
    }
}
