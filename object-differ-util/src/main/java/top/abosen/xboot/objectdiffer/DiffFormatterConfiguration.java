package top.abosen.xboot.objectdiffer;

import lombok.Builder;
import lombok.Getter;

/**
 * @author qiubaisen
 * @date 2023/1/14
 */

@Builder
@Getter
public class DiffFormatterConfiguration implements FormatHandler {
    @Builder.Default
    boolean namingIncludeParent = true;
    @Builder.Default
    String ofWord = "的";
    @Builder.Default
    String fieldSeparator = "；";
    @Builder.Default
    String nullTemplateValue = "空";

    /*单对象的增删是相当于和null对比修改, 容器对象的更具有明确的增删语义*/
    @Builder.Default
    String addedTemplate = "【" + FIELD_NAME + "】从【" + SOURCE_VALUE + "】修改为【" + TARGET_VALUE + "】";
    @Builder.Default
    String changedTemplate = "【" + FIELD_NAME + "】从【" + SOURCE_VALUE + "】修改为【" + TARGET_VALUE + "】";
    @Builder.Default
    String removedTemplate = "【" + FIELD_NAME + "】从【" + SOURCE_VALUE + "】修改为【" + TARGET_VALUE + "】";


    public static final String FIELD_NAME = escape("fieldName");
    public static final String SOURCE_VALUE = escape("sourceValue");
    public static final String TARGET_VALUE = escape("targetValue");

    private static String escape(String template) {
        return "\ue001" + template + "\ue002";
    }


    @Override
    public String formatAdd(String fieldName, Object targetValue) {
        return format(addedTemplate, fieldName, nullTemplateValue, stringify(targetValue));
    }

    @Override
    public String formatChange(String fieldName, Object sourceValue, Object targetValue) {
        return format(changedTemplate, fieldName, stringify(sourceValue), stringify(targetValue));
    }

    @Override
    public String formatRemove(String fieldName, Object sourceValue) {
        return format(removedTemplate, fieldName, stringify(sourceValue), nullTemplateValue);
    }

    private String stringify(Object sourceValue) {
        return sourceValue == null ? nullTemplateValue : String.valueOf(sourceValue);
    }

    private static String format(String template, String fieldName, String sourceValue, String targetValue) {
        return template.replace(FIELD_NAME, fieldName)
                .replace(SOURCE_VALUE, sourceValue)
                .replace(TARGET_VALUE, targetValue);
    }


}


interface FormatHandler {

    String formatAdd(String fieldName, Object targetValue);

    String formatChange(String fieldName, Object sourceValue, Object targetValue);

    String formatRemove(String fieldName, Object sourceValue);
}
