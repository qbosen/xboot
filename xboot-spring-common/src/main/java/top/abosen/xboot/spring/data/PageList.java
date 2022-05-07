package top.abosen.xboot.spring.data;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.data.domain.Page;
import top.abosen.xboot.shared.data.SliceList;
import top.abosen.xboot.shared.utility.CastUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author qiubaisen
 * @date 2021/3/31
 */
public class PageList<T> implements SliceList<T> {
    private int size;
    private List<T> elements;
    private long totalSize;

    public PageList(List<T> elements) {
        this.elements = Objects.isNull(elements)
                ? Collections.emptyList()
                : Collections.unmodifiableList(elements);
        this.size = this.elements.size();
    }

    public static <T> PageList<T> of(List<T> elements) {
        return new PageList<>(elements);
    }

    public static <T> PageList<T> of(Page<T> page) {
        return PageList.of(page.getContent())
                .size(page.getSize())
                .totalSize(page.getTotalElements());
    }

    public static <T> PageList<T> of(IPage<T> page) {
        return PageList.of(page.getRecords())
                .size((int) page.getSize())
                .totalSize(page.getTotal());
    }

    public static <T> PageList<T> empty() {
        return new EmptyPage<>();
    }

    public PageList<T> size(int size) {
        this.size = size;
        return this;
    }

    public PageList<T> totalSize(long totalSize) {
        this.totalSize = totalSize;
        return this;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public int getTotalPages() {
        int size = this.getSize();
        long totalSize = this.getTotalSize();
        return size == 0 ? 1 : (int) Math.ceil((double) totalSize / (double) size);
    }

    @Override
    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    @Override
    public <R> SliceList<R> of(List<R> elements, long totalSize) {
        return new PageList<>(elements).totalSize(totalSize);
    }

    @Override
    public <R> SliceList<R> elements(List<R> elements) {
        return new PageList<>(elements).totalSize(this.totalSize);
    }

    @Override
    public <R> SliceList<R> cast() {
        return CastUtils.cast(this);
    }

    @Override
    public List<T> getElements() {
        return elements;
    }

    public void setElements(List<T> elements) {
        this.elements = elements;
    }

    private static class EmptyPage<E> extends PageList<E> {

        EmptyPage() {
            super(Collections.emptyList());
        }

        @Override
        public <R> SliceList<R> map(Function<E, R> mapper) {
            return CastUtils.cast(this);
        }
    }

}
