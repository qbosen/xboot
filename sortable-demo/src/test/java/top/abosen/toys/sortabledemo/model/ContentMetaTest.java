package top.abosen.toys.sortabledemo.model;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import top.abosen.toys.sortableapi.SortableAutoConfiguration;
import top.abosen.toys.sortableapi.application.SortableCommonService;
import top.abosen.toys.sortableapi.domain.ExecuteMeta;
import top.abosen.toys.sortableapi.domain.SortableElement;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * @author qiubaisen
 * @date 2021/5/4
 */

@SpringBootTest(classes = {
        DataSourceAutoConfiguration.class,
        MybatisAutoConfiguration.class,
        SortableAutoConfiguration.class})
@Slf4j
class ContentMetaTest {
    @Autowired
    SortableCommonService sortableCommonService;


    @ParameterizedTest
    @DisplayName("常规移动")
    @MethodSource("column100provider")
    public void normalTest(ExecuteMeta meta, int fromIdx, int count) {
        moveTest(meta, fromIdx, count);
    }

    static Stream<Arguments> column100provider() {
        final ExecuteMeta meta100 = new ContentMeta().executeMeta(100);
        return Stream.of(
                arguments(meta100, 0, 1),
                arguments(meta100, 1, 1),
                arguments(meta100, 1, 5),
                arguments(meta100, 1, 10),
                arguments(meta100, 1, -10),
                arguments(meta100, 9, -10),
                arguments(meta100, 9, -5)
        );
    }


    @ParameterizedTest
    @DisplayName("超界限的移动")
    @MethodSource("column100_over_bound_provider")
    public void overBoundTest(ExecuteMeta meta, int fromIdx, int count) {
        moveTest(meta, fromIdx, count);
    }

    static Stream<Arguments> column100_over_bound_provider() {
        final ExecuteMeta meta100 = new ContentMeta().executeMeta(100);
        return Stream.of(
                arguments(meta100, 0, 100),
                arguments(meta100, 1, 100),
                arguments(meta100, 5, 100),
                arguments(meta100, 9, 100),
                arguments(meta100, 0, -100),
                arguments(meta100, 1, -100),
                arguments(meta100, 5, -100),
                arguments(meta100, 9, -100)
        );
    }

    @ParameterizedTest
    @DisplayName("有重复权重的移动")
    @MethodSource("column200_duplicate_weight_over_bound_provider")
    public void duplicateOverBoundWeight(ExecuteMeta meta, int fromIdx, int count) {
        moveTest(meta, fromIdx, count);
    }

    static Stream<Arguments> column200_duplicate_weight_over_bound_provider() {
        final ExecuteMeta meta200 = new ContentMeta().executeMeta(200);
        return Stream.of(
                arguments(meta200, 0, 100),
                arguments(meta200, 1, 100),
                arguments(meta200, 5, 100),
                arguments(meta200, 9, 100),
                arguments(meta200, 0, -100),
                arguments(meta200, 1, -100),
                arguments(meta200, 5, -100),
                arguments(meta200, 9, -100)
        );
    }


    @ParameterizedTest
    @DisplayName("有重复权重的移动")
    @MethodSource("column200_duplicate_weight_provider")
    public void duplicateWeight(ExecuteMeta meta, int fromIdx, int count) {
        moveTest(meta, fromIdx, count);
    }

    static Stream<Arguments> column200_duplicate_weight_provider() {
        final ExecuteMeta meta200 = new ContentMeta().executeMeta(200);
        return Stream.of(
                arguments(meta200, 0, 1),
                arguments(meta200, 1, 1),
                arguments(meta200, 1, 5),
                arguments(meta200, 1, 10),
                arguments(meta200, 1, -10),
                arguments(meta200, 9, -10),
                arguments(meta200, 9, -5)
        );
    }


    private void moveTest(ExecuteMeta meta, int fromIdx, int count) {
        List<SortableElement> data;
        Long[] originIds;
        Long[] currentIds;
        Long[] expectCurrentIds;
        SortableElement target;

        {   // 查询数据
            data = sortableCommonService.query(meta, 1, 100);
            if (fromIdx >= data.size()) {
                throw new IllegalArgumentException();
            }
            log.debug("origin sort: {}", data);
            originIds = data.stream().map(SortableElement::getId).toArray(Long[]::new);
        }
        {   // 移动数据
            target = data.get(fromIdx);
            log.debug("move target: {} count: {}", target, count);
            sortableCommonService.move(meta, target.getId(), count);
        }
        {   // 移动后期望的结果
            List<Long> filterIds = Arrays.stream(originIds).filter(it -> it != target.getId()).collect(Collectors.toList());
            int expectIndex = fromIdx + count;
            if (expectIndex < 0) expectIndex = 0;
            if (expectIndex >= data.size()) expectIndex = data.size() - 1;
            filterIds.add(expectIndex, target.getId());
            expectCurrentIds = filterIds.toArray(new Long[0]);
        }
        {   // 移动后的查询
            data = sortableCommonService.query(meta, 1, 100);
            log.debug("current sort: {}", data);
            currentIds = data.stream().map(SortableElement::getId).toArray(Long[]::new);
        }
        log.debug("\nexpect:\t{}\nresult:\t{}", expectCurrentIds, currentIds);
        assertArrayEquals(expectCurrentIds, currentIds);
    }


}