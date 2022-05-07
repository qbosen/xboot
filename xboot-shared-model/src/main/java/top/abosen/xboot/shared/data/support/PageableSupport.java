package top.abosen.xboot.shared.data.support;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import top.abosen.xboot.shared.data.Pageable;

import java.util.Objects;

/**
 * @author qiubaisen
 * @date 2021/3/31
 */
@Getter
@EqualsAndHashCode
public abstract class PageableSupport implements Pageable {
    private Integer page = DEFAULT_PAGE;
    private Integer size = DEFAULT_SIZE;

    public void setPage(Integer page) {
        if (Objects.nonNull(page)) {
            this.page = Math.max(DEFAULT_PAGE, page);
        }
    }

    public void setSize(Integer size) {
        if (Objects.nonNull(size)) {
            this.size = Math.min(Math.max(MIN_SIZE, size), MAX_SIZE);
        }
    }
}
