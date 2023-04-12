package top.abosen.xboot.extensionfield.widget;

import com.google.auto.service.AutoService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author qiubaisen
 * @since 2023/2/27
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AutoService(Widget.class)
@SuperBuilder
@NoArgsConstructor
@Schema(description = "输入框组件")
public class InputWidget extends AbstractWidget {
    public final String type = "input";

    @Schema(description = "是否多行; 当schema是list时生效", example = "[true, false]")
    private boolean multiple;

}
