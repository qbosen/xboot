package top.abosen.xboot.sortableutil.infrastructure.database;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qiubaisen
 * @since 2021/4/29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SortedElementDto {
    long id;
    long weight;
    // 是否置顶，默认为0
    long stick;
    // 是否设置固定行，默认为0
    long row;
}
