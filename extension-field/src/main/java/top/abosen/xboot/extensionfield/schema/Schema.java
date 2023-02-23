package top.abosen.xboot.extensionfield.schema;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.auto.service.AutoService;
import top.abosen.xboot.extensionfield.ValueHolder;
import top.abosen.xboot.extensionfield.ValueHolderChecker;
import top.abosen.xboot.extensionfield.jackson.ParentTypeResolver;

/**
 * @author qiubaisen
 * @date 2023/2/21
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
public interface Schema extends ValueHolderChecker {

    /**
     * 根据自身schema的值定义,可以修改/转换目标值
     *
     * @param holder 值
     */
    default  void resolveValue(ValueHolder holder) {
    }

}

