package top.abosen.dddboot.spring.repository.mybatisplus;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;

/**
 * @author qiubaisen
 * @date 2021/2/4
 */
public class MybatisPlusConfiguration {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        PaginationInnerInterceptor innerInterceptor = new PaginationInnerInterceptor();
        innerInterceptor.setDbType(DbType.MYSQL);

        interceptor.addInnerInterceptor(innerInterceptor);
        return interceptor;
    }

    @Bean
    public SqlInjectEnhancer sqlInjectEnhancer() {
        return new SqlInjectEnhancer();
    }
}
