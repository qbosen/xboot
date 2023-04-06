package top.abosen.xboot.objectdiffer;

import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.node.Visit;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author qiubaisen
 * @since 2023/1/30
 */
public class PrettyLogVisitor implements DiffNode.Visitor {
    private final Object source;
    private final Object target;
    private final DiffFormatterConfiguration configuration;
    private final ValueProviders valueProviders;

    public PrettyLogVisitor(Object source, Object target) {
        this.source = source;
        this.target = target;
        this.configuration = DiffFormatterConfiguration.builder().build();
        this.valueProviders = new ValueProviders();
    }

    public PrettyLogVisitor(Object source, Object target, DiffFormatterConfiguration configuration, ValueProviders valueProviders) {
        this.source = source;
        this.target = target;
        this.configuration = configuration;
        this.valueProviders = valueProviders;
    }

    protected boolean filter(final DiffNode node) {
        return (node.isRootNode() && !node.hasChanges())
                || (node.hasChanges() && !node.hasChildren());
    }

    @Override
    public void node(DiffNode node, Visit visit) {
        Difference difference = new Difference(node, source, target);
        if (difference.isEndPoint()) visit.dontGoDeeper();
//        differences.add(difference);

    }


    public String format(List<Difference> differences) {

        FormatContext context = new FormatContext(configuration, valueProviders, differences);

        return differences.stream()
                .filter(Difference::isDifferent)
                .map(difference -> format(context, difference, fieldName(context, difference)))
                .filter(it -> it.length() != 0)
                .collect(Collectors.joining(configuration.getFieldSeparator()));
    }

    private static String format(FormatContext context, Difference difference, String fieldName) {
        if (fieldName == null || fieldName.length() == 0) {
            return "";
        }
        FormatHandler configuration = context.getConfiguration();
        ValueProviders valueProviders = context.getValueProviders();
        switch (difference.getNode().getState()) {
            case ADDED:
                return configuration.formatAdd(fieldName, targetValue(difference, valueProviders));
            case CHANGED:
                return configuration.formatChange(fieldName, sourceValue(difference, valueProviders), targetValue(difference, valueProviders));
            case REMOVED:
                return configuration.formatRemove(fieldName, sourceValue(difference, valueProviders));
            case UNTOUCHED:
            case CIRCULAR:
            case IGNORED:
            case INACCESSIBLE:
                // ignore
                return "";
        }

        return "";
    }

    private static Object sourceValue(Difference difference, ValueProviders valueProviders) {
        return valueProviders.provideValue(difference.getValueFormat(), difference.getValueType(), difference.getSourceValue());
    }

    private static Object targetValue(Difference difference, ValueProviders valueProviders) {
        return valueProviders.provideValue(difference.getValueFormat(), difference.getValueType(), difference.getTargetValue());

    }


    private static String fieldName(FormatContext context, Difference difference) {
        DiffFormatterConfiguration configuration = context.getConfiguration();
        if (configuration.isNamingIncludeParent()) {
            return context.getPathDisplayName(difference);
        } else {
            return difference.getDisplayName();
        }
    }


    static class PathHolder{

    }

}
