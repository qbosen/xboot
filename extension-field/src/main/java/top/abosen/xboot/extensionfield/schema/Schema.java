package top.abosen.xboot.extensionfield.schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import top.abosen.xboot.extensionfield.ValueHolder;
import top.abosen.xboot.extensionfield.ValueHolderChecker;

/**
 * @author qiubaisen
 * @date 2023/2/21
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
public interface Schema extends ValueHolderChecker {

    @JsonIgnore
    String getType();
    /**
     * 根据自身schema的值定义,可以修改/转换目标值
     *
     * @param holder 值
     */
    default  void resolveValue(ValueHolder holder) {
    }

}

