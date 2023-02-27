package top.abosen.xboot.extensionfield.widget;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
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
public class SelectWidget extends AbstractWidget {
    public static final String TYPE = "select";
    private boolean multiple;
    private BizWidgetExtension biz;

    public SelectWidget() {
        super(TYPE);
    }
    public void setBizString(String bizString) {
        this.biz = new DefaultBizWidgetExtension(bizString);
    }
}
