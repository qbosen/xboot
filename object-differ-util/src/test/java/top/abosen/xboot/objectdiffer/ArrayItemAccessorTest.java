package top.abosen.xboot.objectdiffer;

import de.danielbechler.diff.identity.EqualsIdentityStrategy;
import org.junit.jupiter.api.Test;
import top.abosen.xboot.objectdiffer.Models.ObjectWithIdAndValueByHashEquals;
import top.abosen.xboot.objectdiffer.Models.ObjectWithIdAndValueByIdentityAnno;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static top.abosen.xboot.objectdiffer.DelegateIdentityService.diffIdentityStrategy;

/**
 * @author qiubaisen
 * @since 2023/1/30
 */
public class ArrayItemAccessorTest {
    private static final Object ANY_REFERENCE_ITEM = new Object();

    @Test
    void should_return_item_equal_to_reference_item_by_hash_equals() {
        ArrayItemAccessor accessor = new ArrayItemAccessor(
                new ObjectWithIdAndValueByHashEquals("1", "any"),
                EqualsIdentityStrategy.getInstance());
        Object[] array = new ObjectWithIdAndValueByHashEquals[]{
                new ObjectWithIdAndValueByHashEquals("1", "foo")
        };

        ObjectWithIdAndValueByHashEquals item = (ObjectWithIdAndValueByHashEquals) accessor.get(array);

        assertThat(item.getId()).isEqualTo("1");
        assertThat(item.getValue()).isEqualTo("foo");
    }


    @Test
    void should_return_item_equal_to_reference_item_by_identity_equals() {
        ArrayItemAccessor accessor = new ArrayItemAccessor(
                new ObjectWithIdAndValueByIdentityAnno("1", "any"),
                diffIdentityStrategy(ObjectWithIdAndValueByIdentityAnno.class));
        Object[] array = new ObjectWithIdAndValueByIdentityAnno[]{
                new ObjectWithIdAndValueByIdentityAnno("1", "foo")
        };

        ObjectWithIdAndValueByIdentityAnno item = (ObjectWithIdAndValueByIdentityAnno) accessor.get(array);

        assertThat(item.getId()).isEqualTo("1");
        assertThat(item.getValue()).isEqualTo("foo");
    }


    @Test
    void should_return_null_if_no_item_matches_the_reference() {
        var referenceItemForNonExistingItem = new ObjectWithIdAndValueByHashEquals("1", "any");
        var accessor = new ArrayItemAccessor(referenceItemForNonExistingItem);
        assertThat(accessor.get(new Object[0])).isNull();
    }

    @Test
    void should_return_null_if_reference_item_is_null() {
        Object referenceItem = null;
        var accessor = new ArrayItemAccessor(referenceItem);
        assertThat(accessor.get(new String[]{"some-item"})).isNull();
    }

    @Test
    void should_throw_exception_if_target_object_is_not_an_array() {
        var accessor = new ArrayItemAccessor(ANY_REFERENCE_ITEM);
        var notAnArray = new Object();
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> accessor.get(notAnArray));
    }

    @Test
    void should_throw_exception_if_add_item_into_array() {
        var accessor = new ArrayItemAccessor(new String[]{"foo"});
        String[] target = new String[0];
        assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> accessor.set(target, "foo"));

    }

    @Test
    void should_throw_exception_if_remove_item_from_array() {
        var accessor = new ArrayItemAccessor(new String[]{"foo"});
        String[] target = new String[]{"foo"};

        assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> accessor.unset(target));
    }

}
