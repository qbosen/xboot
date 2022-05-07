package top.abosen.xboot.sortableutil.domain;


import java.util.List;

/**
 * @author qiubaisen
 * @date 2021/5/4
 */
public interface SortableElementRepository {
    void saveSortElements(ExecuteMeta executeMeta, List<SortableElement> elements);

    List<SortableElement> query(SortableQuery query);

    long count(SortableQuery query);

    SortableElement find(ExecuteMeta executeMeta, long idValue);

    long totalCount(ExecuteMeta executeMeta);
}
