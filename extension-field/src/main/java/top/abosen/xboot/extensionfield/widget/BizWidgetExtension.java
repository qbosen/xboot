package top.abosen.xboot.extensionfield.widget;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import top.abosen.xboot.extensionfield.valueholder.ValueHolderChecker;
import top.abosen.xboot.extensionfield.valueholder.ValueHolderUpdater;

/**
 * 对特定业务提供支持
 * <p>
 * ValueHolderChecker 提供存储转换 和 校验
 * <p>
 * ValueHolderUpdater 提供查询转换
 *
 * @author qiubaisen
 * @since 2023/2/27
 */

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "key", visible = true, defaultImpl = DefaultBizWidgetExtension.class)
public interface BizWidgetExtension extends ValueHolderChecker, ValueHolderUpdater {
    String getKey();
}
