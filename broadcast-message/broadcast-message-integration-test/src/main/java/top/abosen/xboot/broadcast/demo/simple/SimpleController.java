package top.abosen.xboot.broadcast.demo.simple;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qiubaisen
 * @since 2023/3/31
 */

@RestController
@RequiredArgsConstructor
@Slf4j
public class SimpleController {
    final SimpleCacheService simpleCacheService;

    @GetMapping("/direct/{id}")
    public void direct(@PathVariable long id) {
        simpleCacheService.clearUserCache(id);
    }


}
