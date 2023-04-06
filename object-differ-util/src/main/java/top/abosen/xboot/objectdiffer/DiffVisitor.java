package top.abosen.xboot.objectdiffer;

import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.node.Visit;

import java.util.LinkedList;
import java.util.List;

/**
 * @author qiubaisen
 * @since 2023/1/14
 */
public class DiffVisitor implements DiffNode.Visitor {
    private final Object source;
    private final Object target;

    private final List<Difference> differences;

    public DiffVisitor(Object source, Object target) {
        this.source = source;
        this.target = target;
        this.differences = new LinkedList<>();
    }

    protected boolean filter(final DiffNode node) {
        return (node.isRootNode() && !node.hasChanges())
                || (node.hasChanges() && !node.hasChildren());
    }

    @Override
    public void node(DiffNode node, Visit visit) {
        Difference difference = new Difference(node, source, target);
        if (difference.isEndPoint()) visit.dontGoDeeper();
        differences.add(difference);
    }

    public List<Difference> getDifferences() {
        return differences;
    }

}
