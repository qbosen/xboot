package top.abosen.xboot.extensionfield.valueholder;

/**
 * @author qiubaisen
 * @since 2023/2/23
 */
public interface ValueHolderUpdater {

    /**
     * 用于返回数据时, 自定义数据的更新
     *
     * @param valueHolder null表示没有对应的key
     */
    void updateValue(ValueHolder valueHolder);

}
