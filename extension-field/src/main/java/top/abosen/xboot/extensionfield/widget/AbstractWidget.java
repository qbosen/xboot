package top.abosen.xboot.extensionfield.widget;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author qiubaisen
 * @since 2023/2/27
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@SuperBuilder
@NoArgsConstructor
public abstract class AbstractWidget implements Widget {
    String name;
    @Schema(description = "样式值, 由前端自定义", example = "[\"dropdown\", \"checkbox\", \"password\"]")
    private String style;
}
