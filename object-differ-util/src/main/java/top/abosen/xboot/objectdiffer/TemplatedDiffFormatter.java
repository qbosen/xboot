package top.abosen.xboot.objectdiffer;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author qiubaisen
 * @date 2023/1/15
 */
class TemplatedDiffFormatter implements DiffFormatter {
    final DiffFormatterConfiguration configuration;
    private final FormatSources formatSources;

    TemplatedDiffFormatter(DiffFormatterConfiguration configuration, FormatSources formatSources) {
        this.configuration = configuration;
        this.formatSources = formatSources;
    }

    @Override
    public String format(List<Difference> differences) {

        FormatContext context = new FormatContext(configuration, formatSources, differences);

        return differences.stream().map(difference -> format(context, difference, fieldName(context, difference)))
                .filter(it -> it.length() != 0)
                .collect(Collectors.joining(configuration.getFieldSeparator()));
    }

    private static String format(FormatContext context, Difference difference, String fieldName) {
        if (fieldName == null || fieldName.length() == 0) {
            return "";
        }
        DiffFormatterConfiguration configuration = context.getConfiguration();
        FormatSources formatSources = context.getFormatSources();
        switch (difference.getNode().getState()) {
            case ADDED:
                return configuration.getTemplate().formatAdd(fieldName, targetValue(difference, formatSources));
            case CHANGED:
                return configuration.getTemplate().formatChange(fieldName, sourceValue(difference, formatSources), targetValue(difference, formatSources));
            case REMOVED:
                return configuration.getTemplate().formatRemove(fieldName, sourceValue(difference, formatSources));
            case UNTOUCHED:
            case CIRCULAR:
            case IGNORED:
            case INACCESSIBLE:
                // ignore
                return "";
        }

        return "";
    }

    private static Object sourceValue(Difference difference, FormatSources formatSources) {
        return formatSources.provideValue(difference.getValueFormat(), difference.getValueType(), difference.getSourceValue());
    }

    private static Object targetValue(Difference difference, FormatSources formatSources) {
        return formatSources.provideValue(difference.getValueFormat(), difference.getValueType(), difference.getTargetValue());

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
