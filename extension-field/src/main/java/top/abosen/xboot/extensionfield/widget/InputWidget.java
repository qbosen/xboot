package top.abosen.xboot.extensionfield.widget;

import com.google.auto.service.AutoService;
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
public class InputWidget extends AbstractWidget {
    public final String type = "input";

    private boolean multiple;

}
