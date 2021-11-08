package top.abosen.toys.sortableapi;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import top.abosen.toys.sortableapi.application.SortableCommonService;
import top.abosen.toys.sortableapi.application.SortableCommonServiceImpl;
import top.abosen.toys.sortableapi.domain.SortableElementRepository;
import top.abosen.toys.sortableapi.infrastructure.SortableRepositoryImpl;
import top.abosen.toys.sortableapi.infrastructure.database.SortableElementMapper;

/**
 * @author qiubaisen
 * @date 2021/4/29
 */

@Configurable
@MapperScan(basePackages = "top.abosen.toys.sortableapi.infrastructure", annotationClass = Mapper.class)
public class SortableAutoConfiguration {
    @Bean
    public SortableElementRepository elementRepository(SortableElementMapper mapper) {
        return new SortableRepositoryImpl(mapper);
    }

    @Bean
    SortableCommonService sortableCommonService(SortableElementRepository sortableElementRepository) {
        return new SortableCommonServiceImpl(sortableElementRepository);
    }
}
