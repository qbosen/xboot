package top.abosen.dddboot.spring.exception;


import top.abosen.dddboot.shared.exception.AppException;

import java.util.Map;

public class RequestValidationException extends AppException {
    public RequestValidationException(Map<String, Object> data) {
        super(SpringCommonErrorCode.REQUEST_VALIDATION_FAILED, data);
    }
}
