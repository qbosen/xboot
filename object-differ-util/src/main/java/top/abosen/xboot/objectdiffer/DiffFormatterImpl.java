package top.abosen.xboot.objectdiffer;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author qiubaisen
 * @date 2023/1/15
 */
class DiffFormatterImpl implements DiffFormatter {
    final DiffFormatterConfiguration configuration;

    DiffFormatterImpl(DiffFormatterConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String format(List<Difference> differences) {

        FormatContext context = new FormatContext(configuration, differences);

        return differences.stream().map(difference -> format(context, difference, fieldName(context, difference)))
                .filter(it -> it.length() != 0)
                .collect(Collectors.joining(configuration.getFieldSeparator()));
    }

    private static String format(FormatContext context, Difference difference, String fieldName) {
        if (fieldName == null || fieldName.length() == 0) {
            return "";
        }
        DiffFormatterConfiguration configuration = context.getConfiguration();
        switch (difference.getNode().getState()) {
            case ADDED:
                return configuration.getTemplate().formatAdd(fieldName, targetValue(difference));
            case CHANGED:
                return configuration.getTemplate().formatChange(fieldName, sourceValue(difference), targetValue(difference));
            case REMOVED:
                return configuration.getTemplate().formatRemove(fieldName, sourceValue(difference));
            case UNTOUCHED:
            case CIRCULAR:
            case IGNORED:
            case INACCESSIBLE:
                // ignore
                return "";
        }

        return "";
    }

    private static String sourceValue(Difference difference) {
        return parseValue(difference, difference.getSourceValue());
    }

    private static String targetValue(Difference difference) {
        return parseValue(difference, difference.getTargetValue());
    }

    private static String parseValue(Difference difference, Object value) {
        return DiffFieldParser.parse(difference.getFunctionClass(), value);
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
