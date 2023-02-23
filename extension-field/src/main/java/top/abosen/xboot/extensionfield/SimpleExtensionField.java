package top.abosen.xboot.extensionfield;

import lombok.Getter;

import java.util.List;

/**
 * @author qiubaisen
 * @date 2023/2/22
 */

@Getter
public class SimpleExtensionField implements ExtensionField{
    String key;
    Schema schema;
    Widget widget;
}

@Getter
class NestedExtensionField implements ExtensionField{
    String key;
    List<ExtensionField> fields;
}

interface ExtensionField{
    String getKey();

    default boolean isNested(){
        return false;
    }
}