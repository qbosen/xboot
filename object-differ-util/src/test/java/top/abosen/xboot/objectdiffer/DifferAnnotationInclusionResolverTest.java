package top.abosen.xboot.objectdiffer;

import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.node.DiffNode;
import lombok.Value;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static de.danielbechler.diff.node.DiffNode.State.IGNORED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author qiubaisen
 * @since 2023/1/13
 */

@SuppressWarnings("ClassCanBeRecord")
class DifferAnnotationInclusionResolverTest {

    @Test
    void should_include_by_default() {
        var working = new ObjectByDefault("working");
        var base = new ObjectByDefault("base");

        DiffNode node = getDiffNode(working, base);
        assertEquals(1, node.childCount());
        assertTrue(node.getChild("name").isChanged());
    }

    @Value
    static class ObjectByDefault {
        String name;
    }

    @Test
    void should_include_if_getter_annotated() {
        var working = new ObjectWithGetterInclusion("working");
        var base = new ObjectWithGetterInclusion("base");

        DiffNode node = getDiffNode(working, base);
        assertEquals(1, node.childCount());
        assertTrue(node.getChild("name").isChanged());
    }

    @Value
    static class ObjectWithGetterInclusion {
        String name;

        @Diff
        public String getName() {
            return name;
        }
    }

    @Test
    void should_include_if_field_annotated() {
        var working = new ObjectWithFieldInclusion("working");
        var base = new ObjectWithFieldInclusion("base");

        DiffNode node = getDiffNode(working, base);
        assertEquals(1, node.childCount());
        assertTrue(node.getChild("name").isChanged());
    }

    @Value
    static class ObjectWithFieldInclusion {
        @Diff
        String name;
    }

    @Test
    void should_exclude_if_getter_annotated_ignore() {
        var working = new ObjectWithGetterExclusion("working");
        var base = new ObjectWithGetterExclusion("base");

        DiffNode node = getDiffNode(working, base, IGNORED);

        assertEquals(1, node.childCount());
        assertTrue(node.getChild("name").isIgnored());
    }

    @Value
    static class ObjectWithGetterExclusion {
        String name;

        @Diff(ignore = true)
        public String getName() {
            return name;
        }
    }

    @Test
    void should_exclude_if_field_annotated_ignore() {
        var working = new ObjectWithFieldExclusion("working");
        var base = new ObjectWithFieldExclusion("base");

        DiffNode node = getDiffNode(working, base, IGNORED);

        assertEquals(1, node.childCount());
        assertTrue(node.getChild("name").isIgnored());
    }

    @Value
    static class ObjectWithFieldExclusion {
        @Diff(ignore = true)
        String name;
    }

    @Test
    void should_include_property_and_ignore_unannotated_siblings() {
        var working = new ObjectWithInclusionAndSibling("working", "working");
        var base = new ObjectWithInclusionAndSibling("base", "base");
        DiffNode node = getDiffNode(working, base, IGNORED);
        assertEquals(2, node.childCount());
        assertTrue(node.getChild("name").isChanged());
        assertTrue(node.getChild("sibling").isIgnored());
    }

    @Value
    static class ObjectWithInclusionAndSibling {
        @Diff
        String name;
        String sibling;
    }

    @DisplayName("ignore的注解 不会影响sibling的默认include行为")
    @Test
    void should_include_sibling_if_property_is_excluded() {
        var working = new ObjectWithExclusionAndSibling("working", "working");
        var base = new ObjectWithExclusionAndSibling("base", "base");
        DiffNode node = getDiffNode(working, base, IGNORED);
        assertEquals(2, node.childCount());
        assertTrue(node.getChild("name").isIgnored());
        assertTrue(node.getChild("sibling").isChanged());
    }

    @Value
    static class ObjectWithExclusionAndSibling {
        @Diff(ignore = true)
        String name;
        String sibling;
    }

    @Test
    void should_include_all_no_anno_fields_but_not_type_annotated_ignore(){
        var working = new ObjectWithFieldTypeIgnore("working", new ObjectTypedIgnore("working"));
        var base = new ObjectWithFieldTypeIgnore("base", new ObjectTypedIgnore("base"));
        DiffNode node = getDiffNode(working, base, IGNORED);
        assertEquals(2, node.childCount());
        assertTrue(node.getChild("wrap").isIgnored());
        assertTrue(node.getChild("name").isChanged());
    }

    @Value
    @Diff(ignore = true)
    static class ObjectTypedIgnore {
        String value;
    }

    @Value
    static class ObjectWithFieldTypeIgnore {
        String name;
        ObjectTypedIgnore wrap;
    }


    @Test
    void should_include_anno_override_type_anno() {
        var working = new ObjectWithOverrideFieldTypeIgnore("working", new ObjectTypedIgnore("working"));
        var base = new ObjectWithOverrideFieldTypeIgnore("base", new ObjectTypedIgnore("base"));
        DiffNode node = getDiffNode(working, base, IGNORED);
        assertEquals(2, node.childCount());
        assertTrue(node.getChild("wrap").isChanged());
        assertTrue(node.getChild("name").isIgnored());
    }

    @Value
    static class ObjectWithOverrideFieldTypeIgnore {
        String name;
        @Diff
        ObjectTypedIgnore wrap;
    }


    private static DiffNode getDiffNode(Object working, Object base, DiffNode.State... lookStates) {
        ObjectDifferBuilder builder = ObjectDifferBuilder.startBuilding();
        builder.inclusion().resolveUsing(new DifferAnnotationInclusionResolver());
        for (DiffNode.State state : lookStates) {
            builder.filtering().returnNodesWithState(state);
        }
        return builder.build().compare(working, base);
    }


}