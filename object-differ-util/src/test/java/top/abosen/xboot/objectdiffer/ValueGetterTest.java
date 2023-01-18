package top.abosen.xboot.objectdiffer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author qiubaisen
 * @date 2023/1/18
 */
class ValueGetterTest {
    @Test
    void should_directly_return_null_when_pass_null_for_filter_null() throws Exception {
        ValueGetter getter = mock(ValueGetter.class);
        when(getter.getValue(any())).thenReturn("");
        assertNull(ValueGetter.filterNull(getter).getValue(null));
    }

    @Test
    void should_return_origin_item_when_pass_non_null_for_filter_null() throws Exception {
        Object argument = new Object();
        ValueGetter getter = mock(ValueGetter.class);
        when(getter.getValue(argument)).thenReturn(argument);
        assertSame(argument, ValueGetter.filterNull(getter).getValue(argument));
    }
}