package com.github.qbosen.sortabledemo.sortable;

import com.github.qbosen.sortabledemo.common.PersistFlag;
import lombok.Data;

/**
 * @author qiubaisen
 * @date 2021/3/22
 */
@Data
public class SortableElement implements PersistFlag {
    private PersistType persistType;
    /**
     * 默认为long类型。
     */
    private long id;
    /**
     * 权重，拖动排序的依据。
     */
    private long weight;
    /**
     * 置顶标志。
     */
    private boolean stick;
    /**
     * 固定行，0表示没有固定行，从1开始。
     */
    private long row;

}
