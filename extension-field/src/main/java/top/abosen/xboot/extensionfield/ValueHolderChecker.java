package top.abosen.xboot.extensionfield;

/**
 * @author qiubaisen
 * @date 2023/2/23
 */
public interface ValueHolderChecker {
    // todo check and save result message

    /**
     *
     * @param valueHolder null表示没有对应的key
     * @return
     */
    boolean checkValue(ValueHolder valueHolder);

}
