package top.abosen.dddboot.sortabledemo.model;

import top.abosen.dddboot.sortableutil.domain.ExecuteMeta;

/**
 * @author qiubaisen
 * @date 2021/5/21
 */
public class ContentStickMeta {
    private static final ExecuteMeta META = ExecuteMeta.stickMeta("content_stick", "id", "weight", "stick");
    private static final String COLUMN_FIELD = "column_id";


    private static String buildCondition(long columnId) {
        return String.format("%s=%d", COLUMN_FIELD, columnId);
    }

    public static ExecuteMeta executeMeta(long columnId) {
        return META.condition(buildCondition(columnId));
    }

}
