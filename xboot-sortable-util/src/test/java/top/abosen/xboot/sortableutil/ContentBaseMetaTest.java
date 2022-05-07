package top.abosen.xboot.sortableutil;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import top.abosen.xboot.sortableutil.application.SortableCommonService;
import top.abosen.xboot.sortableutil.domain.ExecuteMeta;
import top.abosen.xboot.sortableutil.domain.SortableElement;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * @author qiubaisen
 * @date 2021/5/4
 */

@SpringBootTest(classes = {
        DataSourceAutoConfiguration.class,
        SqlInitializationAutoConfiguration.class,
        MybatisAutoConfiguration.class,
        SortableAutoConfiguration.class})
@Slf4j
class ContentBaseMetaTest {
    @Autowired
    SortableCommonService sortableCommonService;

    static Stream<Arguments> column100provider() {
        final ExecuteMeta meta100 = ContentBaseMeta.executeMeta(100);
        return Stream.of(
                Arguments.arguments(meta100, 0, 1),
                Arguments.arguments(meta100, 1, 1),
                Arguments.arguments(meta100, 1, 5),
                Arguments.arguments(meta100, 1, 10),
                Arguments.arguments(meta100, 1, -10),
                Arguments.arguments(meta100, 9, -10),
                Arguments.arguments(meta100, 9, -5)
        );
    }

    static Stream<Arguments> column100_over_bound_provider() {
        final ExecuteMeta meta100 = ContentBaseMeta.executeMeta(100);
        return Stream.of(
                Arguments.arguments(meta100, 0, 100),
                Arguments.arguments(meta100, 1, 100),
                Arguments.arguments(meta100, 5, 100),
                Arguments.arguments(meta100, 9, 100),
                Arguments.arguments(meta100, 0, -100),
                Arguments.arguments(meta100, 1, -100),
                Arguments.arguments(meta100, 5, -100),
                Arguments.arguments(meta100, 9, -100)
        );
    }

    static Stream<Arguments> column200_duplicate_weight_over_bound_provider() {
        final ExecuteMeta meta200 = ContentBaseMeta.executeMeta(200);
        return Stream.of(
                Arguments.arguments(meta200, 0, 100),
                Arguments.arguments(meta200, 1, 100),
                Arguments.arguments(meta200, 5, 100),
                Arguments.arguments(meta200, 9, 100),
                Arguments.arguments(meta200, 0, -100),
                Arguments.arguments(meta200, 1, -100),
                Arguments.arguments(meta200, 5, -100),
                Arguments.arguments(meta200, 9, -100)
        );
    }

    static Stream<Arguments> column200_duplicate_weight_provider() {
        final ExecuteMeta meta200 = ContentBaseMeta.executeMeta(200);
        return Stream.of(
                Arguments.arguments(meta200, 0, 1),
                Arguments.arguments(meta200, 1, 1),
                Arguments.arguments(meta200, 1, 5),
                Arguments.arguments(meta200, 1, 10),
                Arguments.arguments(meta200, 1, -10),
                Arguments.arguments(meta200, 9, -10),
                Arguments.arguments(meta200, 9, -5)
        );
    }

    @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER + ParameterizedTest.DEFAULT_DISPLAY_NAME)
    @DisplayName("常规移动")
    @MethodSource("column100provider")
    public void normalTest(ExecuteMeta meta, int fromIdx, int count) {
        moveTest(meta, fromIdx, count);
    }

    @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER + ParameterizedTest.DEFAULT_DISPLAY_NAME)
    @DisplayName("超界限的移动")
    @MethodSource("column100_over_bound_provider")
    public void overBoundTest(ExecuteMeta meta, int fromIdx, int count) {
        moveTest(meta, fromIdx, count);
    }

    @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER + ParameterizedTest.DEFAULT_DISPLAY_NAME)
    @DisplayName("有重复权重的移动")
    @MethodSource("column200_duplicate_weight_over_bound_provider")
    public void duplicateOverBoundWeight(ExecuteMeta meta, int fromIdx, int count) {
        moveTest(meta, fromIdx, count);
    }

    @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER + ParameterizedTest.DEFAULT_DISPLAY_NAME)
    @DisplayName("有重复权重的移动")
    @MethodSource("column200_duplicate_weight_provider")
    public void duplicateWeight(ExecuteMeta meta, int fromIdx, int count) {
        moveTest(meta, fromIdx, count);
    }

    private void moveTest(ExecuteMeta meta, int fromIdx, int count) {
        List<SortableElement> data;
        Long[] originIds;
        Long[] currentIds;
        Long[] expectCurrentIds;
        SortableElement target;

        {   // 查询数据
            data = sortableCommonService.query(meta, 1, 100).getData();
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
            if (expectIndex < 0) {
                expectIndex = 0;
            }
            if (expectIndex >= data.size()) {
                expectIndex = data.size() - 1;
            }
            filterIds.add(expectIndex, target.getId());
            expectCurrentIds = filterIds.toArray(new Long[0]);
        }
        {   // 移动后的查询
            data = sortableCommonService.query(meta, 1, 100).getData();
            log.debug("current sort: {}", data);
            currentIds = data.stream().map(SortableElement::getId).toArray(Long[]::new);
        }
        log.debug("\nexpect:\t{}\nresult:\t{}", expectCurrentIds, currentIds);
        assertArrayEquals(expectCurrentIds, currentIds);
    }


}