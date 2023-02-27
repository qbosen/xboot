package top.abosen.xboot.extensionfield.widget;

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
@ToString
@EqualsAndHashCode
public abstract class AbstractWidget implements Widget {
    private final String type;

    protected AbstractWidget(String type) {
        this.type = type;
    }
}
