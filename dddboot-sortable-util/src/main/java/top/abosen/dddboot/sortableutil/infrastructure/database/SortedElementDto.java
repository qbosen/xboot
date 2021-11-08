package top.abosen.dddboot.sortableutil.infrastructure.database;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qiubaisen
 * @date 2021/4/29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SortedElementDto {
    long id;
    long weight;

    // 是否设置固定行，默认为0
    long row;
    // 是否置顶，默认为false
    boolean stick;
}
