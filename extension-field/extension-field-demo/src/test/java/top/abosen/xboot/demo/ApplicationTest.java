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
import top.abosen.xboot.extensionfield.extension.*;
import top.abosen.xboot.extensionfield.schema.IntegerSchema;
import top.abosen.xboot.extensionfield.schema.StringSchema;
import top.abosen.xboot.extensionfield.widget.InputWidget;
import top.abosen.xboot.extensionfield.widget.OptionWidget;
import top.abosen.xboot.extensionfield.widget.SelectWidget;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author qiubaisen
 * @since 2023/3/1
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

    private <T> List<T> lists(T... items) {
        return Arrays.asList(items);
    }

    private <K, V> Map<K, V> maps(Object... kvs) {
        LinkedHashMap<K, V> map = new LinkedHashMap<>();
        for (int i = 0; i < kvs.length; i += 2) {
            map.put((K) kvs[i], (V) kvs[i + 1]);
        }
        return map;
    }

    @Test
    void should_add_content_type() throws Exception {
        ContentType contentType = ContentType.builder()
                //region build content type
                .key("article")
                .name("图文文章")
                .fields(new ExtensionFields(lists(
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
                        MapExtensionField.builder()
                                .key("videos")
                                .name("星选视频")
                                .extension(maps(
                                        "desc", "打分,视频,视频标题的复杂组合",
                                        "other", "扩展属性, 业务上可编辑和操作"
                                ))
                                .fields(lists(
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
                                                .extension(maps(
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
                                                        .options(maps(
                                                                "一星", 1,
                                                                "二星", 2,
                                                                "三星", 3,
                                                                "四星", 4,
                                                                "五星", 5
                                                        ))
                                                        .build())
                                                .build(),
                                        ListExtensionField.builder()
                                                .key("videos")
                                                .name("视频列表")
                                                .target(SimpleExtensionField.builder()
                                                        .key("video")
                                                        .name("视频")
                                                        .schema(StringSchema.builder().required(true).build())
                                                        .widget(SelectWidget.builder()
                                                                .name("视频")
                                                                .multiple(true)
                                                                .biz(new VideoBizExtension())
                                                                .build())
                                                        .build()
                                                )
                                                .build()
                                ))
                                .build()
//endregion
                ))).build();

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
                .extension(new ExtensionTypeValueMap(maps(
                        "share_template", "",
                        "videos", maps(
                                "title", "精选视频集合2022",
                                "score", 5,
                                "videos", lists("1000", "1005")
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
        // 根据业务更新自身
        System.out.println("文章更新前;" + queryContent);
        queryContent.getExtension().update(article);
        System.out.println("文章更新后;" + queryContent);

        // template 被设置为默认值
        content.setId(contentId);
        Map<String, Object> newExtension = Maps.newHashMap(content.getExtension().toMap());
        newExtension.put("share_template", "分享了一篇内容");
        // biz 填充信息
        ((Map<String, Object>) newExtension.get("videos")).put("videos", lists(
                maps("id", "1000", "name", "视频名称", "url", "http://www.baidu.com"),
                maps("id", "1005", "name", "视频名称", "url", "http://www.baidu.com")
        ));
        content.setExtension(new ExtensionTypeValueMap(newExtension));
        assertThat(queryContent).isNotNull().isEqualTo(content);
    }
}