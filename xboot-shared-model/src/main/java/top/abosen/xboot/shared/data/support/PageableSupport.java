package top.abosen.xboot.shared.data.support;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import top.abosen.xboot.shared.data.Pageable;

import java.util.Objects;

/**
 * @author qiubaisen
 * @date 2021/3/31
 */
@Getter
@EqualsAndHashCode
@ToString
@SuperBuilder
public abstract class PageableSupport implements Pageable {
    @Builder.Default
    private Integer page = DEFAULT_PAGE;
    @Builder.Default
    private Integer size = DEFAULT_SIZE;

    protected PageableSupport() {
    }

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

    /**
     * ignore the {@link Pageable#MAX_SIZE} limit
     *
     * @param size direct set size
     */
    public void setSizeDirect(Integer size) {
        if (Objects.nonNull(size)) {
            this.size = size;
        }
    }

}
