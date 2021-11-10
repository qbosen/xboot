package top.abosen.dddboot.shared.data.support;

import top.abosen.dddboot.shared.data.Query;
import top.abosen.dddboot.shared.data.Sort;
import lombok.EqualsAndHashCode;

import java.util.Objects;

/**
 * @author qiubaisen
 * @date 2021/3/31
 */

@EqualsAndHashCode(callSuper = true)
public abstract class QuerySupport extends PageableSupport implements Query {
    private Sort sort;

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
}
