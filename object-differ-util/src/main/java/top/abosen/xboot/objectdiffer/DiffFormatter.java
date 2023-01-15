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
 * @date 2023/1/11
 */
interface DiffFormatter {

    String format(List<Difference> differences);
}


