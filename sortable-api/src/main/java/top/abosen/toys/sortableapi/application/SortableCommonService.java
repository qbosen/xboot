package top.abosen.toys.sortableapi.application;

import top.abosen.toys.sortableapi.domain.ExecuteMeta;
import top.abosen.toys.sortableapi.domain.SortableElement;

import java.util.List;

/**
 * @author qiubaisen
 * @date 2021/5/4
 */
public interface SortableCommonService {
    List<SortableElement> query(ExecuteMeta executeMeta, long page, long size);

    /**
     * 移动排序，可能影响中间数据
     *
     * @param executeMeta 可执行的数据元信息
     * @param id          目标数据id
     * @param count       大于0表示下移，小于0表示上移动
     * @return 是否发生了移动
     */
    boolean move(ExecuteMeta executeMeta, long id, int count);

    /**
     * 移动到最顶上，不影响中间数据
     *
     * @param executeMeta 可执行的数据元信息
     * @param id          目标数据id
     * @return 是否发生了移动
     */
    boolean moveToTop(ExecuteMeta executeMeta, long id);
}
