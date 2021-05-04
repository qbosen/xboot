package top.abosen.toys.sortableapi.infrastructure.database;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author qiubaisen
 * @date 2021/4/29
 */
@Mapper
public interface SortableElementMapper {

    void sortElements(@Param("elements") List<SortableElementDto> elements);

    List<SortedElementDto> querySortElement(
            @Param("tableName") String tableName,
            @Param("idField") String idField,
            @Param("weightField") String weightField,

            @Param("condition") String condition,

            @Param("weightBegin") Long weightBegin,
            @Param("weightEnd") Long weightEnd,
            @Param("weightAsc") boolean weightAsc,

            @Param("offset") Long offset,
            @Param("limit") Long limit
    );

    SortedElementDto findSortElement(
            @Param("tableName") String tableName,
            @Param("idField") String idField,
            @Param("weightField") String weightField,

            @Param("condition") String condition,

            @Param("idValue") long idValue);
}
