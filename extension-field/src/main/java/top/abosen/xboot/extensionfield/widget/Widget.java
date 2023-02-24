package top.abosen.xboot.extensionfield.widget;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import top.abosen.xboot.extensionfield.ValueHolderChecker;
import top.abosen.xboot.extensionfield.schema.Schema;

import java.util.List;
import java.util.Set;

/**
 * @author qiubaisen
 * @date 2023/2/21
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
public interface Widget extends ValueHolderChecker {
    default Set<Class<? extends Schema>> supportSchemas(){
        return null;
    }

    default boolean support(Schema schema) {
        return supportSchemas() == null || supportSchemas().contains(schema.getClass());
    }

}
