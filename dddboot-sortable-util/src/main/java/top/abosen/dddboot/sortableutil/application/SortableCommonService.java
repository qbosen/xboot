package top.abosen.dddboot.sortableutil.application;

import top.abosen.dddboot.sortableutil.domain.ExecuteMeta;
import top.abosen.dddboot.sortableutil.domain.PagedList;
import top.abosen.dddboot.sortableutil.domain.SortableElement;

/**
 * @author qiubaisen
 * @date 2021/5/4
 */
public interface SortableCommonService {
    PagedList<SortableElement> query(ExecuteMeta executeMeta, long page, long size);


    /**
     * 移动排序，可能影响中间数据
     * 置顶数据 只可在置顶数据间移动
     * 固定行数据 不可移动
     *
     * @param executeMeta 可执行的数据元信息
     * @param id          目标数据id
     * @param count       大于0表示下移，小于0表示上移动
     * @return 是否发生了移动
     */
    boolean move(ExecuteMeta executeMeta, long id, int count);

    /**
     * 移动到最顶上，不影响中间数据
     * 置顶数据 只可在置顶数据间移动
     * 固定行数据 不可移动
     *
     * @param executeMeta 可执行的数据元信息
     * @param id          目标数据id
     * @return 是否发生了移动
     */
    boolean moveToTop(ExecuteMeta executeMeta, long id);


    boolean stick(ExecuteMeta executeMeta, long id, boolean stick);

    boolean frozenRow(ExecuteMeta executeMeta, long id, long row, boolean frozen);
}
