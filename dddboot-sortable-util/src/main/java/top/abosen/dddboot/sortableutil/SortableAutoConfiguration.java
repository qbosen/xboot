package top.abosen.dddboot.sortableutil;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import top.abosen.dddboot.sortableutil.application.SortableCommonServiceImpl;
import top.abosen.dddboot.sortableutil.domain.SortableElementRepository;
import top.abosen.dddboot.sortableutil.domain.SortableStickWeightDefaultGenerator;
import top.abosen.dddboot.sortableutil.domain.SortableStickWeightGenerator;
import top.abosen.dddboot.sortableutil.infrastructure.database.SortableElementMapper;
import top.abosen.dddboot.sortableutil.application.SortableCommonService;
import top.abosen.dddboot.sortableutil.infrastructure.SortableRepositoryImpl;

/**
 * @author qiubaisen
 * @date 2021/4/29
 */

@Configurable
@MapperScan(basePackages = "top.abosen.dddboot.sortableutil.infrastructure", annotationClass = Mapper.class)
public class SortableAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SortableStickWeightGenerator stickWeightGenerator() {
        return new SortableStickWeightDefaultGenerator();
    }

    @Bean
    public SortableElementRepository elementRepository(SortableElementMapper mapper) {
        return new SortableRepositoryImpl(mapper);
    }

    @Bean
    public SortableCommonService sortableCommonService(SortableElementRepository sortableElementRepository, SortableStickWeightGenerator stickWeightGenerator) {
        return new SortableCommonServiceImpl(sortableElementRepository, stickWeightGenerator);
    }
}
