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
@Schema(description = "三方组件; 从三方选择数据")
public class SelectWidget extends AbstractWidget {
    public final String type = "select";
    @Schema(description = "是否多行; 当schema是list时生效", example = "[true, false]")
    private boolean multiple;
    @Schema(description = "业务组件扩展; 由key定义具体业务, 后台针对不同业务进行相应处理", example = "{\"key\": \"video\"}")
    private BizWidgetExtension biz;
}
