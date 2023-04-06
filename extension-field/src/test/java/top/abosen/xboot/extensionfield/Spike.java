package top.abosen.xboot.extensionfield;

import org.junit.jupiter.api.Test;

import java.lang.annotation.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author qiubaisen
 * @since 2023/2/22
 */
public class Spike {

    @Target({ ElementType.ANNOTATION_TYPE ,ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    @interface Meta{
        String value() default "";
    }
    @Target({ ElementType.ANNOTATION_TYPE ,ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Meta("sub")
    @interface Sub{

    }
    @Sub
    @Meta("direct")
    class Demo{

    }

    @Test
    void should_get_meta_anno(){
        List<Meta> collect = Arrays.stream(Demo.class.getAnnotations()).flatMap(it -> {
            if (it.annotationType() == Meta.class) {
                return Stream.of((Meta)it);
            } else {
                return Arrays.stream(it.annotationType().getAnnotationsByType(Meta.class));
            }
        }).collect(Collectors.toList());

        System.out.println(collect);


        System.out.println(Arrays.toString(Demo.class.getAnnotationsByType(Meta.class)));

    }


}
