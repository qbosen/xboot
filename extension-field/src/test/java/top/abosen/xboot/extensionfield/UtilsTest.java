package top.abosen.xboot.extensionfield;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import top.abosen.xboot.extensionfield.util.Utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Optional;

/**
 * @author qiubaisen
 * @since 2023/2/22
 */
class UtilsTest {

    @Nested
    class AnnoGetter {

        @Test
        void should_get_direct_anno() {
            Optional<Meta> anno = Utils.getAnno(TargetSub.class, Meta.class);
            Assertions.assertThat(anno).isPresent().map(Meta::value).hasValue("sub");
        }

        @Test
        void should_get_direct_anno_if_both_present() {
            Optional<Meta> anno = Utils.getAnno(TargetSubAndMeta.class, Meta.class);
            Assertions.assertThat(anno).isPresent().map(Meta::value).hasValue("meta");
        }

        @Test
        void should_get_meta_anno() {
            Optional<Meta> anno = Utils.getAnno(TargetMeta.class, Meta.class);
            Assertions.assertThat(anno).isPresent().map(Meta::value).hasValue("meta");
        }

        @Test
        void should_return_empty() {
            Optional<Meta> anno = Utils.getAnno(TargetNon.class, Meta.class);
            Assertions.assertThat(anno).isEmpty();
        }
    }


    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
    @interface Meta {
        String value() default "";
    }

    @Meta("sub")
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    @interface Sub {
    }

    static class TargetNon {

    }

    @Sub
    static class TargetSub {

    }

    @Meta("meta")
    static class TargetMeta {

    }

    @Sub
    @Meta("meta")
    static class TargetSubAndMeta {

    }
}
