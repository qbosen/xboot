package top.abosen.xboot.spring.exception;

import top.abosen.xboot.shared.exception.AppException;
import top.abosen.xboot.shared.exception.ErrorCode;

import java.time.Instant;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

/**
 * @author qiubaisen
 * @date 2021/4/2
 */
public class ErrorRepresentation {
    private final int code;
    private final String codeStr;
    private final String message;
    private final String path;
    private final Instant timestamp;
    private final Map<String, Object> data = newHashMap();

    ErrorRepresentation(AppException ex, String path) {
        ErrorCode error = ex.getError();
        this.code = error.getCode();
        this.codeStr = error.getCodeStr();
        this.message = error.getMessage();
        this.path = path;
        this.timestamp = Instant.now();
        if (!ex.getData().isEmpty()) {
            this.data.putAll(ex.getData());
        }
    }

}
