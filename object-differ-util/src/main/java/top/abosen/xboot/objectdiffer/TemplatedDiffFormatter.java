package top.abosen.xboot.objectdiffer;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author qiubaisen
 * @date 2023/1/15
 */
class TemplatedDiffFormatter implements DiffFormatter {
    final DiffFormatterConfiguration configuration;
    private final ValueProviders valueProviders;

    TemplatedDiffFormatter(DiffFormatterConfiguration configuration, ValueProviders valueProviders) {
        this.configuration = configuration;
        this.valueProviders = valueProviders;
    }

    @Override
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
        DiffFormatterConfiguration configuration = context.getConfiguration();
        ValueProviders valueProviders = context.getValueProviders();
        switch (difference.getNode().getState()) {
            case ADDED:
                return configuration.getTemplate().formatAdd(fieldName, targetValue(difference, valueProviders));
            case CHANGED:
                return configuration.getTemplate().formatChange(fieldName, sourceValue(difference, valueProviders), targetValue(difference, valueProviders));
            case REMOVED:
                return configuration.getTemplate().formatRemove(fieldName, sourceValue(difference, valueProviders));
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


}
