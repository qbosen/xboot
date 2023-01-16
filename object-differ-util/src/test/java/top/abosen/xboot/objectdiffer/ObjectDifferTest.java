package top.abosen.xboot.objectdiffer;

import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.node.PrintingVisitor;
import lombok.Value;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
                .visit(new PrintingVisitor(working, base));

    }

    @Test
    void should_diff_user_list() {
        List<User> source = List.of(new User("a", "123"), new User("b", "234"));
        List<User> target = List.of(new User("c", "123"), new User("b", "123"));
        System.out.println(ObjectDiffer.buildDefault().compare(source, target));
    }

    @Value
    static class User {
        @DiffField(name = "用户名")
        String name;
        String password;
    }

    @Nested
    class DiffFieldIdTest {

        @Value
        static class ObjectIdentified {
            @DiffField.Id
            long idButIgnore;

            @DiffField
            String name;
        }

        @Test
        void should_not_be_different_if_id_equals() {
            ObjectDiffer objectDiffer = ObjectDiffer.buildDefault();
            String compare = objectDiffer.compare(new ObjectIdentified(100, "Jack"), new ObjectIdentified(100, "Tom"));
            assertEquals("", compare);
        }

        @Test
        void should_be_different_if_id_not_equals() {
            ObjectDiffer objectDiffer = ObjectDiffer.buildDefault();
            String compare = objectDiffer.compare(new ObjectIdentified(100, "Jack"), new ObjectIdentified(101, "Tom"));
//            assertEquals("", compare);
            System.out.println(compare);
        }
    }

    @Nested
    class FormatSource {
        @Nested
        class MethodSource {

            @Test
            void should_invoke_to_string_if_method_not_present() {
                ObjectDiffer objectDiffer = ObjectDiffer.buildDefault();
                String compare = objectDiffer.compare(new ObjectWithMethodSource(1234, "手机"), new ObjectWithMethodSource(4321, "手机"));
                System.out.println(compare);
            }

            @Value
            @DiffField.Format(source = DiffField.Format.Source.METHOD, methodSource = "customToString")
            static class ObjectWithMethodSource {
                @DiffField.Id
                long id;

                String goods;

                public String customToString() {
                    return "物流订单{单号:%s, 商品:%s}".formatted(id, goods);
                }
            }


        }
    }

}