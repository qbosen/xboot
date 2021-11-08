package top.abosen.dddboot.spring.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import top.abosen.dddboot.shared.exception.AppException;
import top.abosen.dddboot.shared.exception.SystemException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.springframework.http.HttpStatus.valueOf;


@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(AppException.class)
    @ResponseBody
    public ResponseEntity<ErrorRepresentation> handleAppException(AppException ex, HttpServletRequest request) {
        return new ResponseEntity<>(new ErrorRepresentation(ex, request.getRequestURI()), new HttpHeaders(), valueOf(ex.getError().getStatus()));
    }

    @ExceptionHandler({BindException.class})
    @ResponseBody
    public ResponseEntity<ErrorRepresentation> handleInvalidRequest(BindException ex, HttpServletRequest request) {
        String path = request.getRequestURI();

        Map<String, Object> error = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, fieldError -> {
                    String message = fieldError.getDefaultMessage();
                    return isEmpty(message) ? "无错误提示" : message;
                }));

        RequestValidationException exception = new RequestValidationException(error);
        return new ResponseEntity<>(new ErrorRepresentation(exception, path), valueOf(exception.getError().getStatus()));
    }


    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ResponseEntity<?> handleGeneralException(Throwable ex, HttpServletRequest request) {
        String path = request.getRequestURI();
        log.error("Error occurred while access[{}]:", path, ex);
        SystemException exception = new SystemException(ex);
        return new ResponseEntity<>(new ErrorRepresentation(exception, path), valueOf(exception.getError().getStatus()));
    }


}
