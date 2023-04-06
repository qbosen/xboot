package top.abosen.xboot.shared.exception;


import static com.google.common.collect.ImmutableMap.of;

/**
 * @author qiubaisen
 * @since 2021/4/3
 */

public class SystemException extends AppException {

    public SystemException(Throwable cause) {
        super(CommonErrorCode.SYSTEM_ERROR, of("detail", cause.getMessage()), cause);
    }
}