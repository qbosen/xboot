package top.abosen.xboot.spring.exception;

import top.abosen.xboot.shared.exception.ErrorCode;

/**
 * @author qiubaisen
 * @since 2021/4/2
 */
public enum SpringCommonErrorCode implements ErrorCode {
    REQUEST_VALIDATION_FAILED(40001, 500, "参数校验错误"),
    ;
    private final int code;
    private final int status;
    private final String message;

    SpringCommonErrorCode(int code, int status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getCodeStr() {
        return this.name();
    }

    @Override
    public String getMessage() {
        return message;
    }

}
