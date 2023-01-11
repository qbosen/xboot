package top.abosen.xboot.objectdiffer;

import de.danielbechler.diff.node.DiffNode;

/**
 * @author qiubaisen
 * @date 2023/1/11
 */
public class ObjectDiffer {
    DefaultDiffItemFormatter diffItemFormatter;

}

interface DiffItemFormatter {
    String present(DiffNode diffNode, Object o1, Object o2);
}

class DefaultDiffItemFormatter implements DiffItemFormatter {
    @Override
    public String present(DiffNode diffNode, Object o1, Object o2) {
        if (!diffNode.hasChanges()) {
            return "";
        }
        return null;
    }
}