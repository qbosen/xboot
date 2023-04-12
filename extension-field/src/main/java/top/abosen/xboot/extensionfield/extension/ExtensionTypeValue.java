package top.abosen.xboot.extensionfield.extension;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

/**
 * @author qiubaisen
 * @since 2023/2/22
 */
@JsonSerialize(as = ExtensionTypeValueMap.class)
@JsonDeserialize(as = ExtensionTypeValueMap.class)
@Schema(description = "扩展字段值对象, Map<String, List|Map|Value>",
        example = "{\"age\":20,\"hobby-movie\":[{\"name\":\"复仇者联盟\",\"score\":5},{\"name\":\"蜘蛛侠\",\"score\":4}],\"address\":{\"province\":\"广东省\",\"city\":\"深圳市\",\"street\":\"南山区\",\"code\":518000},\"pay-type\":{\"支付宝\":{\"phone\":\"13345678900\",\"password\":\"12345678\"}}}")
public interface ExtensionTypeValue {

    Map<String, Object> toMap();

    boolean valid(ExtensionType type);

}

