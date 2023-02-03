package top.abosen.xboot.objectdiffer;

import lombok.experimental.UtilityClass;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.TypedArgumentConverter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * @author qiubaisen
 * @date 2023/1/30
 */

@UtilityClass
public class ArgumentConverters {
    public static class ToCollection extends TypedArgumentConverter<String, Collection> {

        public ToCollection() {
            super(String.class, Collection.class);
        }

        @Override
        protected Collection<String> convert(String source) throws ArgumentConversionException {
            if (source == null) return Collections.emptyList();
            return Arrays.stream(source.split(",")).collect(Collectors.toList());
        }
    }

    public static class ToArray extends TypedArgumentConverter<String, String[]> {

        public ToArray() {
            super(String.class, String[].class);
        }

        @Override
        protected String[] convert(String source) throws ArgumentConversionException {
            if (source == null) return new String[0];
            return Arrays.stream(source.split(",")).toArray(String[]::new);
        }
    }
}
