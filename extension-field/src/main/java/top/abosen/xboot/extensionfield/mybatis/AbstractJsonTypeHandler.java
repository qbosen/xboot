package top.abosen.xboot.extensionfield.mybatis;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractJsonTypeHandler<T> extends BaseTypeHandler<T> {
    public AbstractJsonTypeHandler() {
    }

    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, this.toJson(parameter));
    }

    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String json = rs.getString(columnName);
        return StringUtils.isBlank(json) ? null : this.parse(json);
    }

    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String json = rs.getString(columnIndex);
        return StringUtils.isBlank(json) ? null : this.parse(json);
    }

    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String json = cs.getString(columnIndex);
        return StringUtils.isBlank(json) ? null : this.parse(json);
    }

    protected abstract T parse(String json);

    protected abstract String toJson(T obj);
}