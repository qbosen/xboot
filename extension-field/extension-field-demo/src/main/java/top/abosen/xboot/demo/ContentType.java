package top.abosen.xboot.demo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.abosen.xboot.extensionfield.extension.ExtensionField;
import top.abosen.xboot.extensionfield.extension.ExtensionType;

import java.util.List;

/**
 * @author qiubaisen
 * @since 2023/2/28
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
