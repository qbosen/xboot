package top.abosen.dddboot.sortableutil.domain;


import java.util.List;

/**
 * @author qiubaisen
 * @date 2021/5/4
 */
public interface SortableElementRepository {
    void saveSortElements(ExecuteMeta executeMeta, List<SortableElement> elements);

    List<SortableElement> query(ExecuteMeta executeMeta,
                                boolean weightAsc, Long weightMin, Long weightMax,
                                Long rowMin, Long rowMax,
                                Boolean stick,
                                Long offset, Long limit);

    SortableElement find(ExecuteMeta executeMeta, long idValue);

    long totalCount(ExecuteMeta executeMeta);
}
