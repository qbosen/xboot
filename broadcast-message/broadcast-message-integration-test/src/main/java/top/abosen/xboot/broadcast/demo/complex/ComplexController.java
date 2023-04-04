package top.abosen.xboot.broadcast.demo.complex;

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
public class ComplexController {
    final FooCacheService fooCacheService;
    final BarCacheService barCacheService;

    @GetMapping("/foo/{id}")
    public void foo(@PathVariable long id) {
        fooCacheService.clearFooCache(id);
    }

    @GetMapping("/bar/{id}")
    public void bar(@PathVariable long id) {
        barCacheService.clearBarCache(id);
    }


}
