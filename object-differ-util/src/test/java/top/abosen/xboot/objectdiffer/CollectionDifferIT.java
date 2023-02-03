package top.abosen.xboot.objectdiffer;

import org.junit.jupiter.api.Test;
import top.abosen.xboot.objectdiffer.Models.*;

import java.util.List;

/**
 * @author qiubaisen
 * @date 2023/1/30
 */
public class CollectionDifferIT {

    @Test
    void should_compare_deep_if_collection_node_is_not_endpoint() {
        ObjectDiffer objectDiffer = ObjectDiffer.buildDefault();
        var source = new ObjectWithCollection(List.of(new ObjectWithString("a")));
        var target = new ObjectWithCollection(List.of(new ObjectWithString("b")));

        List<Difference> difference = objectDiffer.difference(source, target);


    }
}
