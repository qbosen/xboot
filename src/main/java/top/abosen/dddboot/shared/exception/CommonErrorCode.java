package top.abosen.dddboot.shared.exception;

/**
 * @author qiubaisen
 * @date 2021/4/2
 */
public enum CommonErrorCode implements ErrorCode {
    SYSTEM_ERROR(10000, 500, "系统内部错误"),
    NO_PERMISSION(10001, 500, "没有权限"),
    DATA_NOT_FOUND(40000, 500, "数据不存在"),
    ;

    private final int code;
    private final int status;
    private final String message;

    CommonErrorCode(int code, int status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    @Override public int getStatus() {
        return status;
    }

    @Override public int getCode() {
        return code;
    }

    @Override public String getCodeStr() {
        return this.name();
    }

    @Override public String getMessage() {
        return message;
    }

}
