package top.abosen.xboot.sortableutil;

import top.abosen.xboot.sortableutil.domain.ExecuteMeta;

/**
 * @author qiubaisen
 * @since 2021/4/30
 */
public class ContentBaseMeta {

    private static final ExecuteMeta META = ExecuteMeta.base("content_base", "id", "weight");

    private static final String COLUMN_FIELD = "column_id";


    public static ExecuteMeta executeMeta(long columnId) {
        return META.condition(buildCondition(columnId));
    }

    private static String buildCondition(long columnId) {
        return String.format("%s=%d", COLUMN_FIELD, columnId);
    }

}
