package top.abosen.xboot.objectdiffer;

import de.danielbechler.diff.identity.EqualsIdentityStrategy;
import de.danielbechler.diff.identity.IdentityStrategy;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author qiubaisen
 * @since 2023/1/30
 */
public class IdentityStrategyTest {

    @Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @CsvSource(nullValues = "null", textBlock = """
            #a      b       equal
            null,   null,   true
            null,   foo,    false
            foo,    null,   false
            foo,    bar,    false
            foo,    foo,    true
            """)
    @interface IdentityParameterSource {
    }

    @Nested
    class EqualsIdentityStrategyTest {
        IdentityStrategy identityStrategy = EqualsIdentityStrategy.getInstance();

        @ParameterizedTest(name = "equals({0}, {1}) should be {2}")
        @IdentityParameterSource
        void should(String a, String b, boolean equality) {
            assertThat(identityStrategy.equals(a, b)).isEqualTo(equality);
        }
    }

    @Nested
    class FieldIdentityStrategyTest {
        Field field = mock(Field.class);
        IdentityStrategy identityStrategy = new DelegateIdentityService.IdentityFieldIdentityStrategy(List.of(field));

        @ParameterizedTest(name = "equals({0}, {1}) should be {2}")
        @IdentityParameterSource
        void should(String a, String b, boolean equality) throws IllegalAccessException {
            Object sourceA = new Object();
            Object sourceB = new Object();
            when(field.get(eq(sourceA))).thenReturn(a);
            when(field.get(eq(sourceB))).thenReturn(b);
            assertThat(identityStrategy.equals(sourceA, sourceB)).isEqualTo(equality);
        }
    }

    @Nested
    class MethodIdentityStrategyTest {
        Method method = mock(Method.class);
        IdentityStrategy identityStrategy = new DelegateIdentityService.IdentityMethodIdentityStrategy(method);

        @ParameterizedTest(name = "equals({0}, {1}) should be {2}")
        @IdentityParameterSource
        void should(String a, String b, boolean equality) throws IllegalAccessException, InvocationTargetException {
            Object sourceA = new Object();
            Object sourceB = new Object();
            when(method.invoke(eq(sourceA))).thenReturn(a);
            when(method.invoke(eq(sourceB))).thenReturn(b);
            assertThat(identityStrategy.equals(sourceA, sourceB)).isEqualTo(equality);
        }
    }
}
