package top.abosen.xboot.extensionfield.widget;

import com.google.auto.service.AutoService;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author qiubaisen
 * @date 2023/2/27
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AutoService(Widget.class)
public class InputWidget extends AbstractWidget {
    public static final String TYPE = "input";

    private boolean multiple;

    public InputWidget() {
        super(TYPE);
    }


}
