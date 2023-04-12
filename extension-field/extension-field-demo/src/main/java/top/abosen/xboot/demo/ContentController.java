package top.abosen.xboot.demo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author qiubaisen
 * @since 2023/2/28
 */

@RestController
@Tag(name = "内容管理")
@RequestMapping("/content")
@RequiredArgsConstructor
public class ContentController {

    final ContentTypeMapper typeMapper;
    final ContentMapper contentMapper;

    @PostMapping
    @Operation(summary = "创建内容")
    public long create(@RequestBody Content content) {
        ContentType contentType = typeMapper.selectById(content.getContentType());

        if (!content.getExtension().valid(contentType)) {
            return -1;
        }
        content.setId(null);
        contentMapper.insert(content);
        return content.getId();
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新内容")
    public void update(@PathVariable long id, @RequestBody Content content) {
        content.setId(id);
        ContentType contentType = typeMapper.selectById(content.getContentType());

        if (!content.getExtension().valid(contentType)) {
            return;
        }
        contentMapper.updateById(content);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取内容")
    public Content get(@PathVariable long id) {
        return contentMapper.selectById(id);
    }

}
