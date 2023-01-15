package top.abosen.xboot.objectdiffer;

import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.differ.CollectionDiffer;
import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.node.PrintingVisitor;
import lombok.Value;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author qiubaisen
 * @date 2023/1/13
 */
class ObjectDifferTest {
    @Test
    void should_diff_list() {
        List<Integer> working = List.of(1, 2, 3);
        List<Integer> base = List.of(2, 3, 1);
        ObjectDifferBuilder.startBuilding().build().compare(working, base)
                .visit(new PrintingVisitor(working,base));

    }

    @Test
    void should_diff_user_list() {
        List<User> source = List.of(new User("a","123"), new User("b","234"));
        List<User> target = List.of(new User("c","123"), new User("b","123"));
        System.out.println(new ObjectDiffer().compare(source, target));
    }

    @Value
    static class User{
        @DiffField(name = "用户名")
        String name;
        String password;
    }

}