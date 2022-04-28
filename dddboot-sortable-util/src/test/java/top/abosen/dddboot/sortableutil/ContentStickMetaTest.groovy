package top.abosen.dddboot.sortableutil


import lombok.extern.slf4j.Slf4j
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import top.abosen.dddboot.sortableutil.application.SortableCommonService
import top.abosen.dddboot.sortableutil.infrastructure.SqlMapper

@SpringBootTest(classes = [
        DataSourceAutoConfiguration.class,
        SqlInitializationAutoConfiguration.class,
        MybatisAutoConfiguration.class,
        SortableAutoConfiguration.class]
)
@Slf4j
class ContentStickMetaTest extends Specification {
    @Autowired
    SortableCommonService sortableCommonService
    @Autowired
    SqlMapper sqlMapper

    def setup() {
        sqlMapper.insert("""
            insert into content_stick(id,column_id,stick,weight)
            values (1,100,0,1),
            (2,100,0,2),
            (3,100,0,3),
            (4,100,0,4),
            (5,100,0,5),
            (6,100,0,6),
            (7,100,0,7),
            (8,100,0,8),
            (9,100,0,9),
            (10,100,0,10)                                                  
        """)
    }

    def cleanup() {
        sqlMapper.delete("""
            delete from content_stick
        """)
    }

    def "常规置顶操作"() {
        given:
        def meta = ContentStickMeta.executeMeta(100)
        when: "2,3 置顶"
        sortableCommonService.stick(meta, 2, true)
        sortableCommonService.stick(meta, 3, true)
        def data = sortableCommonService.query(meta, 1, 10).getData()
        then: "3后置顶,在2前面,剩下的最高的是10"
        data.id == [3, 2, 10, 9, 8, 7, 6, 5, 4, 1]

        when: "取消置顶:2"
        sortableCommonService.stick(meta, 2, false)
        data = sortableCommonService.query(meta, 1, 10).getData()
        then: "3,10,9,8,7,6,5,4,2,1"
        data.id == [3, 10, 9, 8, 7, 6, 5, 4, 2, 1]

        when: "取消置顶:3"
        sortableCommonService.stick(meta, 3, false)
        data = sortableCommonService.query(meta, 1, 10).getData()
        then: "10,9,8,7,6,5,4,3,2,1"
        data.id == [10, 9, 8, 7, 6, 5, 4, 3, 2, 1]

        when: "取消置顶:10"
        sortableCommonService.stick(meta, 10, false)
        data = sortableCommonService.query(meta, 1, 10).getData()
        then: "10,9,8,7,6,5,4,3,2,1"
        "10并没有置顶,无影响"
        data.id == [10, 9, 8, 7, 6, 5, 4, 3, 2, 1]
    }

    def "置顶 和 移动"() {
        given: "2,3 置顶"
        def meta = ContentStickMeta.executeMeta(100)
        def data
        sortableCommonService.stick(meta, 2, true)
        sortableCommonService.stick(meta, 3, true)

        when: "移动非置顶元素1往上6位"
        "[1] 4 5 6 7 8 9 10 (2,3)"
        "4 5 6 7 8 9 [1] 10 (2,3)"
        sortableCommonService.move(meta, 1, -6)
        data = sortableCommonService.query(meta, 1, 10).getData()
        then: "3 2 10 1 9 8 7 6 5 4"
        data.id == [3, 2, 10, 1, 9, 8, 7, 6, 5, 4]

        when: "继续移动[1]往上3位"
        "4 5 6 7 8 9 [1] 10 (2,3)"
        "4 5 6 7 8 9 10 [1] (2,3)"
        sortableCommonService.move(meta, 1, -3)
        data = sortableCommonService.query(meta, 1, 10).getData()
        then: "3 2 1 10 9 8 7 6 5 4"
        "无法超过置顶元素"
        data.id == [3, 2, 1, 10, 9, 8, 7, 6, 5, 4]


        when: "(2) 往上移动2位, 10往下移动2位"
        "4 5 6 7 8 9 [10] 1 (2,3)"
        "4 5 6 7 [10] 8 9 1 (3,2)"
        sortableCommonService.move(meta, 2, -2)
        sortableCommonService.move(meta, 10, 2)
        data = sortableCommonService.query(meta, 1, 10).getData()
        then: "2 3 1 9 8 10 7 6 5 4"
        data.id == [2, 3, 1, 9, 8, 10, 7, 6, 5, 4]

        when: "取消置顶(2) (3)"
        "4 5 6 7 [10] 8 9 1 (3,2)"
        "2 3 4 5 6 7 [10] 8 9 1"
        "置顶的权重 和 排序权重是分开的 所以2,3自身权重不变"
        sortableCommonService.stick(meta, 2, false)
        sortableCommonService.stick(meta, 3, false)
        data = sortableCommonService.query(meta, 1, 10).getData()
        then: "1 9 8 10 7 6 5 4 3 2"
        data.id == [1, 9, 8, 10, 7, 6, 5, 4, 3, 2]
    }


}
