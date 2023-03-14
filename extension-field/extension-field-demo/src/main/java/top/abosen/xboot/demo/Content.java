package top.abosen.xboot.demo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.abosen.xboot.extensionfield.extension.ExtensionTypeValue;

/**
 * @author qiubaisen
 * @date 2023/2/28
 */

@TableName(value = "content", autoResultMap = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Content {
    @TableId(type = IdType.AUTO)
    Long id;

    String contentType;

    String title;
    String body;

    @TableField(typeHandler = JacksonTypeHandler.class)
    ExtensionTypeValue extension;

}
