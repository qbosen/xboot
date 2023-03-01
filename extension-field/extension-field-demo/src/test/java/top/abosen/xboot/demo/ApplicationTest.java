package top.abosen.xboot.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import top.abosen.xboot.extensionfield.extension.ExtensionTypeValueMap;
import top.abosen.xboot.extensionfield.extension.NestedExtensionField;
import top.abosen.xboot.extensionfield.extension.SimpleExtensionField;
import top.abosen.xboot.extensionfield.schema.IntegerSchema;
import top.abosen.xboot.extensionfield.schema.LongListSchema;
import top.abosen.xboot.extensionfield.schema.StringSchema;
import top.abosen.xboot.extensionfield.widget.InputWidget;
import top.abosen.xboot.extensionfield.widget.OptionWidget;
import top.abosen.xboot.extensionfield.widget.SelectWidget;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author qiubaisen
 * @date 2023/3/1
 */
@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ContentTypeMapper typeMapper;
    @Autowired
    private ContentMapper contentMapper;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void should_add_content_type() throws Exception {
        ContentType contentType = ContentType.builder()
                //region build content type
                .key("article")
                .name("图文文章")
                .fields(List.of(
                        SimpleExtensionField.builder()
                                .key("share_template")
                                .name("分享模板")
                                .schema(StringSchema.builder()
                                        .required(false)
                                        .defaultValue("分享了一篇内容")
                                        .maxLength(10)
                                        .build())
                                .widget(InputWidget.builder()
                                        .name("输入框")
                                        .build())
                                .build(),
                        NestedExtensionField.builder()
                                .key("videos")
                                .name("星选视频")
                                .extension(Map.of(
                                        "desc", "打分,视频,视频标题的复杂组合",
                                        "other", "扩展属性, 业务上可编辑和操作"
                                ))
                                .fields(List.of(
                                        SimpleExtensionField.builder()
                                                .key("title")
                                                .name("视频组名称")
                                                .schema(StringSchema.builder()
                                                        .required(true)
                                                        .minLength(1)
                                                        .maxLength(10)
                                                        .build())
                                                .widget(InputWidget.builder()
                                                        .name("输入框")
                                                        .build())
                                                .build(),
                                        SimpleExtensionField.builder()
                                                .key("score")
                                                .name("评分")
                                                .extension(Map.of(
                                                        "desc", "OptionWidget的style由页面定义"
                                                ))
                                                .schema(IntegerSchema.builder()
                                                        .required(true)
                                                        .min(0)
                                                        .max(5)
                                                        .defaultValue(3)
                                                        .build())
                                                .widget(OptionWidget.builder()
                                                        .name("下拉框")
                                                        .style("dropdown")
                                                        .options(Map.of(
                                                                "一星", 1,
                                                                "二星", 2,
                                                                "三星", 3,
                                                                "四星", 4,
                                                                "五星", 5
                                                        ))
                                                        .build())
                                                .build(),
                                        SimpleExtensionField.builder()
                                                .key("videos")
                                                .name("视频列表")
                                                .schema(LongListSchema.builder()
                                                        .required(true)
                                                        .maxSize(10)
                                                        .build())
                                                .widget(SelectWidget.builder()
                                                        .name("视频")
                                                        .multiple(true)
                                                        .biz(new VideoBizExtension())
                                                        .build())
                                                .build()
                                ))
                                .build()
//endregion
                )).build();

        String json = objectMapper.writeValueAsString(contentType);
        System.out.println(json);

        mockMvc.perform(MockMvcRequestBuilders.post("/type")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(MockMvcResultMatchers.status().isOk());

        ContentType article = typeMapper.selectById("article");
        System.out.println(article);
        assertThat(article).isNotNull()
                .isEqualTo(contentType);

        // 创建文章
        Content content = Content.builder()
                .contentType("article")
                .title("测试文章")
                .body("正文...")
                .extension(new ExtensionTypeValueMap(Map.of(
                        "share_template", "",
                        "videos", List.of(
                                Map.of("title", "精选视频集合2022"),
                                Map.of("score", 5),
                                Map.of("videos", List.of(1000, 1005))
                        ))
                )).build();

        String contentJson = objectMapper.writeValueAsString(content);
        System.out.println(contentJson);

        String response = mockMvc.perform(MockMvcRequestBuilders.post("/content")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        long contentId = Long.parseLong(response);
        Content queryContent = contentMapper.selectById(contentId);
        System.out.println(queryContent);

        // template 被设置为默认值
        content.setId(contentId);
        Map<String, Object> newExtension = Maps.newHashMap(content.getExtension().toMap());
        newExtension.put("share_template", "分享了一篇内容");
        content.setExtension(new ExtensionTypeValueMap(newExtension));
        assertThat(queryContent).isNotNull().isEqualTo(content);
    }
}