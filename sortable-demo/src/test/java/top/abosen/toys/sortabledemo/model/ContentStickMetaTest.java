package top.abosen.toys.sortabledemo.model;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import top.abosen.toys.sortableapi.SortableAutoConfiguration;
import top.abosen.toys.sortableapi.application.SortableCommonService;

/**
 * @author qiubaisen
 * @date 2021/5/21
 */

@Slf4j
class ContentStickMetaTest {


    @SpringBootTest(classes = {DataSourceAutoConfiguration.class, MybatisAutoConfiguration.class, SortableAutoConfiguration.class})
    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class Basic_stick_feature_test {
        @Autowired
        SortableCommonService sortableCommonService;

        @DisplayName("常规内容置顶")
        @ParameterizedTest(name = "For example, year {0} is not supported.")
        @MethodSource("")
        public void normal_stick_content() {
//            sortableCommonService.query();
        }
    }
}
