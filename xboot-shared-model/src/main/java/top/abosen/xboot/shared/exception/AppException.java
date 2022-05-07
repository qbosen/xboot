package top.abosen.xboot.shared.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * Base exception class for all business exceptions
 *
 * @author qiubaisen
 * @date 2021/4/2
 */

public abstract class AppException extends RuntimeException {
    private final ErrorCode error;
    private final Map<String, Object> data = new HashMap<>();

    protected AppException(ErrorCode code, Map<String, Object> data) {
        super(format(code.getCodeStr(), code.getMessage(), data));
        this.error = code;
        if (!data.isEmpty()) {
            this.data.putAll(data);
        }
    }

    protected AppException(ErrorCode code, Map<String, Object> data, Throwable cause) {
        super(format(code.getCodeStr(), code.getMessage(), data), cause);
        this.error = code;
        if (!data.isEmpty()) {
            this.data.putAll(data);
        }
    }

    private static String format(String code, String message, Map<String, Object> data) {
        return data.isEmpty() ? String.format("%s.", message) :
                String.format("%s:%s.", message, data.toString());
    }

    public ErrorCode getError() {
        return error;
    }

    public Map<String, Object> getData() {
        return data;
    }
}
