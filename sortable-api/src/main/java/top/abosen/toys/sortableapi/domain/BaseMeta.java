package top.abosen.toys.sortableapi.domain;

import lombok.Value;

/**
 * @author qiubaisen
 * @date 2021/5/4
 */
@Value
public class BaseMeta {
    String tableName;
    String idField;
    String sortField;
}
