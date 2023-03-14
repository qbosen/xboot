package top.abosen.xboot.extensionfield.validator;

/**
 * 值校验器
 * <p>
 * 除了显式的空值校验器,其他校验器都应该忽略空值,即校验通过
 */
public interface ValueValidator extends Validatable {
    boolean valid(Object value);

}
