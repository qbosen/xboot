package top.abosen.xboot.broadcast;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author qiubaisen
 * @since 2023/3/14
 */


@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface BroadcastMessage {
    /**
     * 类型名称, 默认为className
     *
     * @return 类型名称
     */
    String[] value();
}
