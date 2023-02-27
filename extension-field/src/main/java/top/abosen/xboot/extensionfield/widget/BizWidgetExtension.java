package top.abosen.xboot.extensionfield.widget;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import top.abosen.xboot.extensionfield.valueholder.ValueHolderChecker;

/**
 * 对特定业务提供支持
 * @author qiubaisen
 * @date 2023/2/27
 */

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "key", visible = true, defaultImpl = DefaultBizWidgetExtension.class)
public interface BizWidgetExtension extends ValueHolderChecker {
    String getKey();
}
