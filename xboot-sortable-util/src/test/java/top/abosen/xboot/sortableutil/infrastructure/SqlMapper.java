package top.abosen.xboot.sortableutil.infrastructure;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
interface SqlMapper {
    @Insert("${sql}")
    int insert(String sql);

    @Update("${sql}")
    int update(String sql);

    @Delete("${sql}")
    int delete(String sql);
}
