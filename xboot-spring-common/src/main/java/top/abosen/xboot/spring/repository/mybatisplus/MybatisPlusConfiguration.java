package top.abosen.xboot.spring.repository.mybatisplus;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author qiubaisen
 * @since 2021/2/4
 */
public class MybatisPlusConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        PaginationInnerInterceptor innerInterceptor = new PaginationInnerInterceptor();
        innerInterceptor.setDbType(DbType.MYSQL);

        interceptor.addInnerInterceptor(innerInterceptor);
        return interceptor;
    }

    @Bean
    @ConditionalOnMissingBean
    public SqlInjectEnhancer sqlInjectEnhancer() {
        return new SqlInjectEnhancer();
    }
}
