package top.abosen.xboot.extensionfield.jackson;

import java.util.List;

/**
 * 通过SPI在 {@code /META-INF/services/top.abosen.xboot.extensionfield.jackson.ParentTypeResolver}
 * 中配置需要被动态解析的父类. {@link DynamicSubtypeModule} 将会通过 SPI 加载这些父类所拥有的子类型
 *
 * @author qiubaisen
 * @since 2023/2/22
 */
public interface ParentTypeResolver {
    List<Class<?>> getParentTypes();
}
