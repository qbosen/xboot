package top.abosen.xboot.spring.repository.mybatisplus;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * @author qiubaisen
 * @date 2021/2/22
 */
public class ExistMethod extends AbstractMethod {
    protected ExistMethod() {
        super("exist");
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql = String.format(
                CustomSqlMethod.EXIST.getSql(),
                tableInfo.getTableName(),
                sqlWhereEntityWrapper(true, tableInfo));

        // 动态SQL标签处理器
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);

        // 交给mp处理执行
        return this.addSelectMappedStatementForOther(mapperClass, CustomSqlMethod.EXIST.getMethod(), sqlSource, boolean.class);
    }
}
