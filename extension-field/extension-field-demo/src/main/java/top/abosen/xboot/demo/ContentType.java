package top.abosen.xboot.demo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.abosen.xboot.extensionfield.extension.ExtensionField;
import top.abosen.xboot.extensionfield.extension.ExtensionType;
import top.abosen.xboot.extensionfield.extension.NestedExtensionField;
import top.abosen.xboot.extensionfield.extension.SimpleExtensionField;

import java.util.List;

/**
 * @author qiubaisen
 * @date 2023/2/28
 */

@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
@TableName(value = "content_type", autoResultMap = true)
public class ContentType implements ExtensionType {

    @TableId(value = "`key`",type = IdType.ASSIGN_ID)
    String key;

    String name;

    @TableField(typeHandler = ExtensionFieldTypeHandler.class)
    List<ExtensionField> fields;

}
