package top.abosen.xboot.sortableutil.domain;

import lombok.Getter;

import javax.annotation.Nullable;

/**
 * @author qiubaisen
 * @date 2021/4/30
 */

@Getter
public class ExecuteMeta {
    private final String tableName;
    private final String idField;
    private final String weightField;
    @Nullable
    private final String stickField;
    @Nullable
    private final String rowField;
    @Nullable
    private final String condition;

    private ExecuteMeta(String tableName,
                        String idField,
                        String weightField,
                        @Nullable String stickField,
                        @Nullable String rowField,
                        @Nullable String condition) {
        this.tableName = tableName;
        this.idField = idField;
        this.weightField = weightField;
        this.stickField = stickField;
        this.rowField = rowField;
        this.condition = condition;
    }

    public static ExecuteMeta base(String tableName, String idField, String weightField) {
        return new ExecuteMeta(tableName, idField, weightField, null, null, null);
    }

    public static ExecuteMeta stickMeta(String tableName, String idField, String weightField, String stickField) {
        return new ExecuteMeta(tableName, idField, weightField, stickField, null, null);
    }

    public static ExecuteMeta rowMeta(String tableName, String idField, String weightField, String rowField) {
        return new ExecuteMeta(tableName, idField, weightField, null, rowField, null);
    }

    public static ExecuteMeta stickRowMeta(String tableName, String idField, String weightField, String stickField, String rowField) {
        return new ExecuteMeta(tableName, idField, weightField, stickField, rowField, null);
    }

    public boolean hasStickAbility() {
        return stickField != null && stickField.length() > 0;
    }

    public boolean hasRowFixAbility() {
        return rowField != null && rowField.length() > 0;
    }

    public ExecuteMeta condition(String condition) {
        return new ExecuteMeta(tableName, idField, weightField, stickField, rowField, condition);
    }

    @Override
    public String toString() {
        return "Table[ " + tableName + " ]";
    }
}
