package top.abosen.toys.sortabledemo.model;

import top.abosen.toys.sortableapi.domain.BaseMeta;
import top.abosen.toys.sortableapi.domain.ExecuteMeta;

/**
 * @author qiubaisen
 * @date 2021/4/30
 */
public class ContentMeta {

    private static final BaseMeta META = new BaseMeta("content", "id", "weight");

    private static final String COLUMN_FIELD = "column_id";


    private String buildCondition(long columnId) {
        return String.format("%s=%d", COLUMN_FIELD, columnId);
    }

    public ExecuteMeta executeMeta(long columnId) {
        return ExecuteMeta.builder(META)
                .condition(buildCondition(columnId))
                .build();
    }

}
