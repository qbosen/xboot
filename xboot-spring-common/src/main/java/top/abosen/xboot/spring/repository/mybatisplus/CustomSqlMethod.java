package top.abosen.xboot.spring.repository.mybatisplus;

import lombok.Getter;

/**
 * @author qiubaisen
 * @since 2021/2/4
 */
@Getter
public enum CustomSqlMethod {
    /**
     * 校验数据是否存在
     */
    EXIST("exist", "根据 entity 条件查看是否存在", "<script>\nselect IFNULL((select 1 from %s %s limit 1), 0)\n</script>");

    private final String method;
    private final String desc;
    private final String sql;

    CustomSqlMethod(String method, String desc, String sql) {
        this.method = method;
        this.desc = desc;
        this.sql = sql;
    }

}
