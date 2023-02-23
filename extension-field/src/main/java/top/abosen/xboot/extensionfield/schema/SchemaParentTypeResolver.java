package top.abosen.xboot.extensionfield.schema;

import cn.hutool.core.collection.ListUtil;
import com.google.auto.service.AutoService;
import top.abosen.xboot.extensionfield.jackson.ParentTypeResolver;

import java.util.Collections;
import java.util.List;

/**
 * @author qiubaisen
 * @date 2023/2/23
 */
@AutoService(ParentTypeResolver.class)
public class SchemaParentTypeResolver implements ParentTypeResolver{
    @Override
    public List<Class<?>> getParentTypes() {
        return ListUtil.of(Schema.class);
    }
}
