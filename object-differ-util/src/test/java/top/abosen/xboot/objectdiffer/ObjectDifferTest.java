package top.abosen.xboot.objectdiffer;

import de.danielbechler.diff.ObjectDiffer;
import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.node.DiffNode;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author qiubaisen
 * @date 2023/1/11
 */
class ObjectDifferTest {

    //TODO: support ignore fields
    //TODO: support describe fields
    //TODO: support describe fields with OpenAPI annotation
    //TODO: object with map fields should diff map values

    @Test
    void spike() {
        Map<String, String> working = Map.of("item", "foo");
        Map<String, String> base = Map.of("item", "bar");
        ObjectDifferBuilder objectDifferBuilder = ObjectDifferBuilder.startBuilding();
        ObjectDiffer objectDiffer = objectDifferBuilder.build();
        DiffNode diffNode = objectDiffer.compare(base, working);
        diffNode.visit((node, visit) -> System.out.println(node.getPath() + " => " + node.getState()));
    }

}