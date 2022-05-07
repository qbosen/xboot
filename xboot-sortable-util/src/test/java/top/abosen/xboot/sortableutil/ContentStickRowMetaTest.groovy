package top.abosen.xboot.sortableutil

import lombok.extern.slf4j.Slf4j
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import top.abosen.xboot.sortableutil.application.SortableCommonService
import top.abosen.xboot.sortableutil.infrastructure.SqlMapper

@SpringBootTest(classes = [
        DataSourceAutoConfiguration.class,
        SqlInitializationAutoConfiguration.class,
        MybatisAutoConfiguration.class,
        SortableAutoConfiguration.class]
)
@Slf4j
class ContentStickRowMetaTest extends Specification {
    @Autowired
    SortableCommonService sortableCommonService
    @Autowired
    SqlMapper sqlMapper

    def setup() {
        sqlMapper.insert("""
            insert into content_stick_row(id,column_id,stick,`row`,weight)
            values (1,100,0,0,1),
            (2,100,0,0,2),
            (3,100,0,0,3),
            (4,100,0,0,4),
            (5,100,0,0,5),
            (6,100,0,0,6),
            (7,100,0,0,7),
            (8,100,0,0,8),
            (9,100,0,0,9),
            (10,100,0,0,10)                                                  
        """)
    }

    def cleanup() {
        sqlMapper.delete("""
            delete from content_stick_row
        """)
    }

    def "常规固定行操作"() {
        given:
        def meta = ContentStickRowMeta.executeMeta(100)
        def data
        when: "固定{3}到第1行"
        "1 2 3 4 5 6 7 8 9 10"
        "1 2 4 5 6 7 8 9 10 {3}"
        sortableCommonService.frozenRow(meta, 3, 1, true)
        data = sortableCommonService.query(meta, 1, 4).getData()
        then: "3 10 9 8"
        data[0].id == 3
        data[1].id == 10
        data[2].id == 9
        data[3].id == 8

        when: "把(6)置顶, 固定行优先级高于置顶"
        "1 2 4 5 6 7 8 9 10 {3}"
        "1 2 4 5 7 8 9 10 (6) {3}"

        sortableCommonService.stick(meta, 6, true)
        data = sortableCommonService.query(meta, 1, 4).getData()
        then: "3 6 10 9"
        data[0].id == 3
        data[1].id == 6
        data[2].id == 10
        data[3].id == 9

        when: "把{10}固定到11行,把{8}固定到第8行"
        "1 2 4 5 7 8 9 10 (6) {3}"
        "{10} 1 {8} 2 4 5 7 9 (6) {3}"
        sortableCommonService.frozenRow(meta, 10, 11, true)
        sortableCommonService.frozenRow(meta, 8, 8, true)
        data = sortableCommonService.query(meta, 2, 5).getData()
        then: "4 2 8 1 10"
        data[0].id == 4
        data[1].id == 2
        data[2].id == 8
        data[3].id == 1
        data[4].id == 10

        when: "把{4}固定到第1行,非覆盖模式"
        "{10} 1 {8} 2 4 5 7 9 (6) {3}"
        "{10} 1 {8} 2 4 5 7 9 (6) {3}"
        sortableCommonService.frozenRow(meta, 4, 1, false)
        data = sortableCommonService.query(meta, 1, 5).getData()
        then: "3 6 9 7 5"
        data.id == [3, 6, 9, 7, 5]

        when: "把{4}固定到第1行,覆盖模式"
        "{10} 1 {8} 2 4 5 7 9 (6) {3}"
        "{10} 1 {8} 2 3 5 7 9 (6) {4}"
        sortableCommonService.frozenRow(meta, 4, 1, true)
        data = sortableCommonService.query(meta, 1, 10).getData()

        then: "4 6 9 7 5 3 2 8 1 10"
        data.id == [4, 6, 9, 7, 5, 3, 2, 8, 1, 10]

    }

    def "置顶 移动 固定"() {
        given:
        def meta = ContentStickRowMeta.executeMeta(100)
        def data
        when: "固定{3}到第1行, {10}固定到20行, [7]置顶"
        "1 2 3 4 5 6 7 8 9 10"
        "{10} 1 2 4 5 6 8 9 [7] {3}"
        sortableCommonService.frozenRow(meta, 3, 1, true)
        sortableCommonService.frozenRow(meta, 10, 20, true)
        sortableCommonService.stick(meta, 7, true)
        data = sortableCommonService.query(meta, 1, 10).getData()
        then: "3 7 9 8 6 5 4 2 1 10"
        data.id == [3, 7, 9, 8, 6, 5, 4, 2, 1, 10]

        when: "{3}往下移动2行, [7]往下移动3行, 9往下移动2行"
        "{10} 1 2 4 5 6 8 9 [7] {3}"
        "{10} 1 2 4 5 9 6 8 [7] {3}"
        "置顶行移动只在置顶中发生; 固定行不可移动"
        sortableCommonService.move(meta, 3, 2)
        sortableCommonService.move(meta, 7, 3)
        sortableCommonService.move(meta, 9, 2)
        data = sortableCommonService.query(meta, 1, 10).getData()
        then: "3 7 8 6 9 5 4 2 1 10"
        data.id == [3, 7, 8, 6, 9, 5, 4, 2, 1, 10]

        when: "把[2]置顶, {4}覆盖固定到第1行"
        "{10} 1 2 4 5 9 6 8 [7] {3}"
        "{10} 1 3 5 9 6 8 [7] [2] {4}"
        sortableCommonService.stick(meta, 2, true)
        sortableCommonService.frozenRow(meta, 4, 1, true)
        data = sortableCommonService.query(meta, 1, 10).getData()
        then: "4 2 7 8 6 9 5 3 1 10"
        data.id == [4, 2, 7, 8, 6, 9, 5, 3, 1, 10]

        when: "3往下移2, 7往上移动1, 6往上移动2"
        "{10} 1 3 5 9 6 8 [7] [2] {4}"
        "{10} 3 1 5 9 8 6 [2] [7] {4}"
        sortableCommonService.move(meta, 3, 2)
        sortableCommonService.move(meta, 7, -1)
        sortableCommonService.move(meta, 6, -2)
        data = sortableCommonService.query(meta, 1, 10).getData()
        then: "4 7 2 6 8 9 5 1 3 10"
        data.id == [4, 7, 2, 6, 8, 9, 5, 1, 3, 10]
    }


}
