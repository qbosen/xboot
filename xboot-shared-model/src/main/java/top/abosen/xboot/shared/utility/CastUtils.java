package top.abosen.xboot.shared.utility;

/**
 * @author qiubaisen
 * @since 2021/4/2
 */
public class CastUtils {
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object object) {
        return (T) object;
    }
}
