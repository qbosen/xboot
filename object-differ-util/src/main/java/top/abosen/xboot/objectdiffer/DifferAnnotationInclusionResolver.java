package top.abosen.xboot.objectdiffer;

import de.danielbechler.diff.access.PropertyAwareAccessor;
import de.danielbechler.diff.inclusion.Inclusion;
import de.danielbechler.diff.inclusion.InclusionResolver;
import de.danielbechler.diff.instantiation.TypeInfo;
import de.danielbechler.diff.node.DiffNode;

import static java.util.Collections.emptyList;

/**
 * @author qiubaisen
 * @date 2023/1/13
 */
public class DifferAnnotationInclusionResolver implements InclusionResolver {
    @Override
    public Inclusion getInclusion(DiffNode node) {
        Diff diff = Annotations.getNodeAnno(node, Diff.class);
        if (diff != null) {
            return diff.ignore() ? Inclusion.EXCLUDED : Inclusion.INCLUDED;
        }
        if (hasIncludedSibling(node)) {
            return Inclusion.EXCLUDED;
        }
        return Inclusion.DEFAULT;
    }

    private static boolean hasIncludedSibling(final DiffNode node) {
        for (final PropertyAwareAccessor accessor : getSiblingAccessors(node)) {
            Diff annotation = accessor.getReadMethodAnnotation(Diff.class);
            if (annotation == null) {
                annotation = accessor.getFieldAnnotation(Diff.class);
            }

            if (annotation != null && !annotation.ignore()) {
                return true;
            }
        }
        return false;
    }

    private static Iterable<PropertyAwareAccessor> getSiblingAccessors(final DiffNode node) {
        final DiffNode parentNode = node.getParentNode();
        if (parentNode != null) {
            final TypeInfo typeInfo = parentNode.getValueTypeInfo();
            if (typeInfo != null) {
                return typeInfo.getAccessors();
            }
        }
        return emptyList();
    }

    @Override
    public boolean enablesStrictIncludeMode() {
        return false;
    }
}
