package top.abosen.xboot.objectdiffer;

import de.danielbechler.diff.node.DiffNode;

import java.lang.annotation.Annotation;

/**
 * @author qiubaisen
 * @since 2023/1/17
 */
public class Annotations {
    public static <T extends Annotation> T getNodeAnno(DiffNode node, Class<T> anno) {
        T diff = node.getPropertyAnnotation(anno);
        if (diff == null) {
            diff = node.getFieldAnnotation(anno);
        }
        if (diff == null && node.getValueType() != null) {
            diff = node.getValueType().getAnnotation(anno);
        }
        return diff;
    }
}
