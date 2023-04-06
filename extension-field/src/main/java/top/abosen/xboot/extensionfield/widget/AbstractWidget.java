package top.abosen.xboot.extensionfield.widget;

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
}
