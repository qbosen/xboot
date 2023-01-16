package top.abosen.xboot.objectdiffer;

import lombok.Value;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.annotation.Annotation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author qiubaisen
 * @date 2023/1/16
 */
public class FormatSourceTest {

    record FormatLiteral(Source source, String methodSource, String instanceSource) implements DiffField.Format {
        @Override
        public Class<? extends Annotation> annotationType() {
            return DiffField.Format.class;
        }
    }



    @Nested
    class MethodSourceTest {

        public static final String PUBLIC_MEMBER_METHOD = "publicMemberMethod";
        public static final String PUBLIC_PARENT_METHOD = "publicParentMethod";
        public static final String PUBLIC_PARENT_OVERRIDE_METHOD = "publicParentOverrideMethod";
        public static final String PRIVATE_MEMBER_METHOD = "privateMemberMethod";
        public static final String PUBLIC_MEMBER_METHOD_WITH_PARAMS = "publicMemberMethodWithParams";
        public static final String PUBLIC_STATIC_METHOD = "publicStaticMethod";
        public static final String PRIVATE_STATIC_METHOD = "privateStaticMethod";

        static class Target extends TargetParent {


            public Object publicMemberMethod() {
                return PUBLIC_MEMBER_METHOD;
            }

            @Override
            public Object publicParentOverrideMethod() {
                return PUBLIC_PARENT_OVERRIDE_METHOD;
            }

            private Object privateMemberMethod() {
                return PRIVATE_MEMBER_METHOD;
            }

            public Object publicMemberMethodWithParams(Object args) {
                return PUBLIC_MEMBER_METHOD_WITH_PARAMS;
            }

            public static Object publicStaticMethod() {
                return PUBLIC_STATIC_METHOD;
            }

            private static Object privateStaticMethod() {
                return PRIVATE_STATIC_METHOD;
            }
        }

        static class TargetParent {
            public Object publicParentMethod() {
                return PUBLIC_PARENT_METHOD;
            }

            public Object publicParentOverrideMethod() {
                return "should override";
            }
        }

        @Test
        void should_access_method_belong_target() {
            assertEquals(PUBLIC_MEMBER_METHOD, FormatSource.access(new Target(), PUBLIC_MEMBER_METHOD));
        }

        @Test
        void should_access_parent_public_member_method() {
            assertEquals(PUBLIC_PARENT_METHOD, FormatSource.access(new Target(), PUBLIC_PARENT_METHOD));
        }

        @Test
        void should_invoke_override_parent_member_method() {
            assertEquals(PUBLIC_PARENT_OVERRIDE_METHOD, FormatSource.access(new Target(), PUBLIC_PARENT_OVERRIDE_METHOD));
        }

        @Test
        void should_throw_exception_if_access_private_method() {
            assertThrows(Exception.class, () -> FormatSource.access(new Target(), PRIVATE_MEMBER_METHOD));
        }

        @Test
        void should_throw_exception_if_method_not_found() {
            assertThrows(Exception.class, () -> FormatSource.access(new Target(), "method_not_exist"));
        }

        @Test
        void should_throw_exception_if_method_declared_with_parameter() {
            assertThrows(Exception.class, () -> FormatSource.access(new Target(), PUBLIC_MEMBER_METHOD_WITH_PARAMS));
        }

        @Test
        void should_access_public_static_method() {
            assertEquals(PUBLIC_STATIC_METHOD, FormatSource.access(new Target(), PUBLIC_STATIC_METHOD));
        }

        @Test
        void should_throw_exception_if_access_private_static_method() {
            assertThrows(Exception.class, () -> FormatSource.access(new Target(), PRIVATE_STATIC_METHOD));
        }

        @Test
        void should_access_static_method_even_target_is_null() {
            assertEquals(PUBLIC_STATIC_METHOD, FormatSource.access(Target.class, null, PUBLIC_STATIC_METHOD));
        }
    }

    @Nested
    class InstanceSource {
        @Test
        void should_invoke_instance_provide_method() {
            FormatProvider provider = Mockito.mock(FormatProvider.class);
            Class<?> type = Object.class;
            Object target = mock(Object.class);
            String sourceName = "some name";

            when(provider.name()).thenReturn(sourceName);
            when(provider.filter(any(), any())).thenReturn(true);

            FormatSource formatSource = new FormatSource();
            formatSource.registerInstance(provider);
            FormatLiteral format = new FormatLiteral(DiffField.Format.Source.INSTANCE, "", sourceName);
            formatSource.provideValue(format, type, target);
            verify(provider).provide(type, target);
        }
    }
}
