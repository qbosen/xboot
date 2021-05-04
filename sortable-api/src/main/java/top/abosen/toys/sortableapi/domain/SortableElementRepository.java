package top.abosen.toys.sortableapi.domain;


import org.springframework.lang.Nullable;

import java.util.List;

/**
 * @author qiubaisen
 * @date 2021/5/4
 */
public interface SortableElementRepository {
    void saveSortElements(BaseMeta baseMeta, List<SortableElement> elements);

    List<SortableElement> query(ExecuteMeta executeMeta, boolean weightAsc,
                                @Nullable Long weightBegin, @Nullable Long weightEnd,
                                @Nullable Long offset, @Nullable Long limit);

    SortableElement find(ExecuteMeta executeMeta, long idValue);
}
