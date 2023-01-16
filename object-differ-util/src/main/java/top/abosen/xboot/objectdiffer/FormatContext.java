package top.abosen.xboot.objectdiffer;

import de.danielbechler.diff.node.DiffNode;
import lombok.Value;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author qiubaisen
 * @date 2023/1/15
 */
@Value
class FormatContext {
    DiffFormatterConfiguration configuration;
    Map<DiffNode, Difference> differenceMap;
    FormatSource formatSource;

    public FormatContext(DiffFormatterConfiguration configuration, FormatSource formatSource, List<Difference> differences) {
        this.configuration = configuration;
        this.formatSource = formatSource;
        this.differenceMap = differences.stream().collect(Collectors.toMap(Difference::getNode, Function.identity()));
    }

    public String getPathDisplayName(Difference difference) {
        LinkedList<String> path = new LinkedList<>();
        DiffNode current = difference.getNode();
        while (current != null) {
            Optional.ofNullable(differenceMap.get(current)).map(Difference::getDisplayName).ifPresent(path::addFirst);
            current = current.getParentNode();
        }

        return path.stream().collect(Collectors.joining(configuration.getOfWord()));
    }
}
