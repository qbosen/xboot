package top.abosen.xboot.spring.repository.mybatisplus.typehandler;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author qiubaisen
 * @since 2021/2/22
 */

@MappedTypes({List.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class LongListTypeHandler extends BaseTypeHandler<List<Long>> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Long> parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, Joiner.on(",").skipNulls().join(parameter));
    }

    @Override
    public List<Long> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return this.stringToList(rs.getString(columnName));
    }

    @Override
    public List<Long> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return this.stringToList(rs.getString(columnIndex));
    }

    @Override
    public List<Long> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return this.stringToList(cs.getString(columnIndex));
    }


    @SuppressWarnings("UnstableApiUsage")
    private List<Long> stringToList(String str) {
        return Strings.isNullOrEmpty(str) ? new ArrayList<>() : Splitter.on(",").splitToStream(str).map(Long::parseLong).collect(Collectors.toList());
    }
}