package top.abosen.xboot.objectdiffer;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.UtilityClass;

import java.util.Collection;

/**
 * @author qiubaisen
 * @since 2023/1/30
 */
@UtilityClass
public class Models {
    @Value
    public static class ObjectWithIdAndValueByIdentityAnno {
        @DiffIdentity
        String id;
        String value;
    }

    @Value
    @EqualsAndHashCode(of = "id")
    public static class ObjectWithIdAndValueByHashEquals {
        String id;
        String value;
    }

    @Value
    public class ObjectWithString {
        String value;
    }

    @Value
    public class ObjectWithCollection {
        Collection collection;
    }
}
