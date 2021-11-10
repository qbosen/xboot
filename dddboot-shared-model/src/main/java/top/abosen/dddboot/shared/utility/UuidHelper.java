package top.abosen.dddboot.shared.utility;

import java.util.UUID;

/**
 * @author qiubaisen
 * @date 2021/3/31
 */
public class UuidHelper {
    public enum Type {
        DIGITS,
        BRACES,
        PARENTHESES,
        ORIGIN
    }

    /**
     * 生成UUID.
     *
     * @param type UUID格式
     *             N:digits 01CFF9AE643F43C09D39FA8927C5DB26
     *             D(default):hyphens 01CFF9AE-643F-43C0-9D39-FA8927C5DB26
     *             B:braces {01CFF9AE-643F-43C0-9D39-FA8927C5DB26}
     *             P:parentheses (01CFF9AE-643F-43C0-9D39-FA8927C5DB26)
     */

    public static String generateUuid(Type type) {
        String id = UUID.randomUUID().toString();
        switch (type) {
            case DIGITS:
                return id.replace("-", "");
            case BRACES:
                return String.format("{%s}", id);
            case PARENTHESES:
                return String.format("(%s)", id);
            default:
                return id;
        }
    }

    public static String generateUuid() {
        return generateUuid(Type.ORIGIN);
    }
}
