package top.abosen.xboot.sortableutil;

import top.abosen.xboot.sortableutil.domain.ExecuteMeta;

/**
 * @author qiubaisen
 * @since 2021/5/21
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
