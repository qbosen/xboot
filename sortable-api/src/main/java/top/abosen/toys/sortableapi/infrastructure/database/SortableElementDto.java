package top.abosen.toys.sortableapi.infrastructure.database;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qiubaisen
 * @date 2021/4/29
 */

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
 public class SortableElementDto {
    private String tableName;
    private String idField;
    private String sortField;

    private long idValue;
    private long sortValue;
}
