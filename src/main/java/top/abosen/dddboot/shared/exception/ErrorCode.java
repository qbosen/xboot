package top.abosen.dddboot.shared.exception;

/**
 * @author qiubaisen
 * @date 2021/3/31
 */
public interface ErrorCode {
    /**
     * 状态码
     */
    int getStatus();

    /**
     * 错误码
     */
    int getCode();

    /**
     * 错误码字面
     */
    String getCodeStr();

    String getMessage();

    /**
     * 模板错误信息支持
     *
     * @param args 实际参数
     * @return 错误消息
     */
    default String getMessage(Object[] args) {
        return getMessage();
    }
}
