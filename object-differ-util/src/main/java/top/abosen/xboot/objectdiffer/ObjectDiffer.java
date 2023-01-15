package top.abosen.xboot.objectdiffer;

import de.danielbechler.diff.ObjectDifferBuilder;
import lombok.AllArgsConstructor;

/**
 * @author qiubaisen
 * @date 2023/1/15
 */
@AllArgsConstructor
public class ObjectDiffer {
    final de.danielbechler.diff.ObjectDiffer objectDiffer;
    final DiffFormatter formatter;

    public static Builder builder() {
        return new Builder();
    }
    static class Builder{

    }

    public String compare(Object source, Object target) {
        DiffVisitor visitor = new DiffVisitor(source, target);
        objectDiffer.compare(target, source).visit(visitor);
        return formatter.format(visitor.getDifferences());
    }

    public ObjectDiffer() {
        objectDiffer = defaultObjectDiffer();
        formatter = defaultDiffFormatter();
    }

    public static DiffFormatter defaultDiffFormatter() {
        return new DiffFormatterImpl(defaultFormatterConfiguration());
    }

    private static DiffFormatterConfiguration defaultFormatterConfiguration() {
        return DiffFormatterConfiguration.builder().build();
    }

    private static de.danielbechler.diff.ObjectDiffer defaultObjectDiffer() {
        return ObjectDifferBuilder.startBuilding()
                .inclusion().resolveUsing(new DifferFieldAnnotationInclusionResolver()).and()
                .build();
    }


}
