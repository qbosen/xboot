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
@Tag(name = "内容类型管理")
@RequestMapping("/type")
@RequiredArgsConstructor
public class ContentTypeController {

    final ContentTypeMapper mapper;

    @PostMapping
    @Operation(summary = "创建类型")
    public String create(@RequestBody ContentType type){
        Optional<String> valid = type.validMessage();
        if(valid.isPresent()) return valid.get();

        mapper.insert(type);
        return "OK";
    }

    @PutMapping("/{key}")
    @Operation(summary = "更新类型")
    public String update(@PathVariable String key, @RequestBody ContentType type){
        Optional<String> valid = type.validMessage();
        if(valid.isPresent()) return valid.get();

        type.setKey(key);
        mapper.updateById(type);
        return "OK";
    }


}
