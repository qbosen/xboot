package top.abosen.xboot.extensionfield.extension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.google.common.collect.ImmutableMap;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import top.abosen.xboot.extensionfield.schema.IntegerSchema;
import top.abosen.xboot.extensionfield.schema.StringSchema;
import top.abosen.xboot.extensionfield.widget.InputWidget;
import top.abosen.xboot.extensionfield.widget.OptionWidget;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author qiubaisen
 * @since 2023/2/27
 */
class ExtensionFieldTest {
    ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    @Test
    void should_map_to_simple_field_by_default() throws JsonProcessingException {
        ExtensionField extensionField = objectMapper.readValue("{\n" +
                "  \"key\": \"some\",\n" +
                "  \"foo\": \"bar\"\n" +
                "}", ExtensionField.class);
        assertThat(extensionField).isInstanceOf(SimpleExtensionField.class)
                .hasFieldOrPropertyWithValue("key", "some")
                .extracting(ExtensionField::extension).hasFieldOrPropertyWithValue("foo", "bar");
    }

    private Map<String, Object> desc(String desc) {
        return ImmutableMap.of("desc", desc);
    }

    private <T> LinkedHashMap<String, T> maps(Object... options) {
        LinkedHashMap<String, T> map = new LinkedHashMap<>();
        for (int i = 0; i < options.length; i += 2) {
            map.put(String.valueOf(options[i]), (T) (options[i + 1]));
        }
        return map;
    }

    @Test
    @SneakyThrows
    void simple_demo() {
        SimpleExtensionField ageField = SimpleExtensionField.builder()
                .key("age").name("年龄").extension(desc("一个用于填写年龄的简单扩展"))
                .schema(IntegerSchema.builder().min(0).max(150).required(true).defaultValue(20).build())
                .widget(InputWidget.builder().multiple(false).name("输入年龄").build())
                .build();
        Map<String, Object> ageValue = maps("age", 20);

        ListExtensionField moviesField = ListExtensionField.builder().key("hobby-movie").name("爱好电影").maxSize(10).extension(desc("爱好电影列表,包含名字和评分,最多10个"))
                .target(MapExtensionField.builder().key("nested-hobby").fields(
                        Arrays.asList(
                                SimpleExtensionField.builder().key("name").name("名称")
                                        .schema(StringSchema.builder().minLength(1).maxLength(20).required(true).build())
                                        .widget(InputWidget.builder().multiple(false).name("电影名").build())
                                        .build(),
                                SimpleExtensionField.builder().key("score").name("评分")
                                        .schema(IntegerSchema.builder().min(0).max(5).required(true).defaultValue(3).build())
                                        .widget(OptionWidget.builder().style("star").multiple(false).name("五星评分").options(
                                                maps("一星", 1, "二星", 2, "三星", 3, "四星", 4, "五星", 5)
                                        ).build())
                                        .build()
                        )
                ).build())
                .build();
        Map<String, Object> moviesValue = maps("hobby-movie", Arrays.asList(
                maps("name", "复仇者联盟", "score", 5),
                maps("name", "蜘蛛侠", "score", 4)
        ));


        MapExtensionField addressField = MapExtensionField.builder().key("address").name("地址").extension(desc("填写地址, 组合了多个元素"))
                .fields(Arrays.asList(
                        SimpleExtensionField.builder().key("province").name("省份")
                                .schema(StringSchema.builder().minLength(1).maxLength(20).required(true).build())
                                .widget(InputWidget.builder().multiple(false).name("输入省份").build())
                                .build(),
                        SimpleExtensionField.builder().key("city").name("城市")
                                .schema(StringSchema.builder().minLength(1).maxLength(20).required(true).build())
                                .widget(InputWidget.builder().multiple(false).name("输入城市").build())
                                .build(),
                        SimpleExtensionField.builder().key("street").name("街道")
                                .schema(StringSchema.builder().minLength(1).maxLength(20).required(true).build())
                                .widget(InputWidget.builder().multiple(false).name("输入街道").build())
                                .build(),
                        SimpleExtensionField.builder().key("code").name("邮编")
                                .schema(IntegerSchema.builder().min(0).max(999999).required(true).defaultValue(100000).build())
                                .widget(InputWidget.builder().multiple(false).name("输入邮编").build())
                                .build()
                ))
                .build();
        Map<String, Object> addressValue = maps("address", maps(
                "province", "广东省",
                "city", "深圳市",
                "street", "南山区",
                "code", 518000
        ));


        SwitchExtensionField payField = SwitchExtensionField.builder().key("pay-type").name("支付方式").extension(desc("任选一种支付方式即可"))
                .options(maps("支付宝", MapExtensionField.builder().key("alipay").name("支付宝设置").fields(Arrays.asList(
                                SimpleExtensionField.builder().key("phone").name("帐号")
                                        .schema(StringSchema.builder().required(true).regex("^1[3-9]\\d{9}$").build())
                                        .widget(InputWidget.builder().multiple(false).name("输入支付宝帐号").build())
                                        .build(),
                                SimpleExtensionField.builder().key("password").name("密码")
                                        .schema(StringSchema.builder().required(true).minLength(6).maxLength(20).build())
                                        .widget(InputWidget.builder().multiple(false).style("password").name("输入支付宝密码").build())
                                        .build()
                        )).build()
                        , "银行卡", MapExtensionField.builder().key("credit").name("银行卡设置").fields(Arrays.asList(
                                SimpleExtensionField.builder().key("no").name("卡号")
                                        .schema(StringSchema.builder().required(true).regex("^([1-9]{1})(\\d{14}|\\d{18})$").build())
                                        .widget(InputWidget.builder().multiple(false).name("输入卡号").build())
                                        .build(),
                                SimpleExtensionField.builder().key("password").name("密码")
                                        .schema(StringSchema.builder().required(true).minLength(6).maxLength(6).build())
                                        .widget(InputWidget.builder().multiple(false).style("password").name("输入密码").build())
                                        .build()
                        )).build()))
                .build();
        Map<String, Object> payValue = maps("pay-type", maps("支付宝",
                maps("phone", "13345678900", "password", "12345678")));

        ExtensionFields fields = new ExtensionFields(Arrays.asList(ageField, moviesField, addressField, payField));
        System.out.println("******");
        System.out.println(objectMapper.writeValueAsString(fields));
        System.out.println("******");

        Map<String, Object> maps = new LinkedHashMap<String, Object>() {{
            putAll(ageValue);
            putAll(moviesValue);
            putAll(addressValue);
            putAll(payValue);
        }};

        ExtensionTypeValue valueMap = new ExtensionTypeValueMap(maps);
        System.out.println(objectMapper.writeValueAsString(valueMap));
        System.out.println("******");

        assertThat(valueMap.valid(fields)).isTrue();
    }
}