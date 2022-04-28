package top.abosen.dddboot.sortableutil.infrastructure.database;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author qiubaisen
 * @date 2021/4/29
 */
@Mapper
public interface SortableElementMapper {

    void saveElements(
            @Param("tableName") String tableName,
            @Param("idField") String idField,
            @Param("weightField") String weightField,

            @Nullable @Param("stickField") String stickField,
            @Nullable @Param("rowField") String rowField,

            @Nullable @Param("condition") String condition,
            @Param("elements") List<SortedElementDto> elements);

    /**
     * 查询排序元素
     *
     * @param tableName   表名
     * @param idField     主键字段名
     * @param weightField 权重字段名
     * @param stickField  置顶字段名，null表示无置顶字段
     * @param rowField    固定行字段名，null表示无置顶字段
     * @param condition   排序纬度条件，null表示无条件
     * @param weightMin   权重最小值，可相等。null表示无限制
     * @param weightMax   权重最大值，可相等。null表示无限制
     * @param weightAsc   排序方向，true：权重增序，false：权重降序
     * @param rowMin      固定行最小值，可相等，固定行字段存在时生效。null表示无限制
     * @param rowMax      固定行最大值，可相等，固定行字段存在时生效。null表示无限制
     * @param stick       是否是置顶状态，置顶字段存在时生效。null表示无限制
     * @param offset      位移，offset、limit均不为null时生效
     * @param limit       单页大小，offset、limit均不为null时生效
     * @return 查询数据结果
     */
    List<SortedElementDto> querySortElement(
            @Param("tableName") String tableName,
            @Param("idField") String idField,
            @Param("weightField") String weightField,

            @Nullable @Param("stickField") String stickField,
            @Nullable @Param("rowField") String rowField,

            @Nullable @Param("condition") String condition,

            @Nullable @Param("weightMin") Long weightMin,
            @Nullable @Param("weightMax") Long weightMax,
            @Param("weightAsc") boolean weightAsc,

            @Nullable @Param("rowMin") Long rowMin,
            @Nullable @Param("rowMax") Long rowMax,
            @Nullable @Param("stick") Boolean stick,

            @Nullable @Param("offset") Long offset,
            @Nullable @Param("limit") Long limit
    );

    long countSortElement(
            @Param("tableName") String tableName,
            @Param("idField") String idField,
            @Param("weightField") String weightField,

            @Nullable @Param("stickField") String stickField,
            @Nullable @Param("rowField") String rowField,

            @Nullable @Param("condition") String condition,

            @Nullable @Param("weightMin") Long weightMin,
            @Nullable @Param("weightMax") Long weightMax,
            @Param("weightAsc") boolean weightAsc,

            @Nullable @Param("rowMin") Long rowMin,
            @Nullable @Param("rowMax") Long rowMax,
            @Nullable @Param("stick") Boolean stick
    );

    SortedElementDto findSortElement(
            @Param("tableName") String tableName,
            @Param("idField") String idField,
            @Param("weightField") String weightField,

            @Nullable @Param("stickField") String stickField,
            @Nullable @Param("rowField") String rowField,

            @Nullable @Param("condition") String condition,

            @Param("idValue") long idValue);

    long totalCount(@Param("tableName") String tableName,
                    @Nullable @Param("condition") String condition);
}
