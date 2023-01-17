package top.abosen.xboot.objectdiffer;

import de.danielbechler.diff.identity.IdentityStrategy;

/**
 * @author qiubaisen
 * @date 2023/1/17
 */
public class DiffIdentityAnnotationIdentityStrategy implements IdentityStrategy {
    @Override
    public boolean equals(Object working, Object base) {
        return false;
    }
}
