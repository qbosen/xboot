package top.abosen.xboot.statemachine;

/**
 * Visitable
 *
 * @author qiubaisen
 * @since 2023/5/12
 */
public interface Visitable {
    String accept(final Visitor visitor);
}
