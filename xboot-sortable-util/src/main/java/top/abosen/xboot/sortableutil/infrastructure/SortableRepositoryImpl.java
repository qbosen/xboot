package top.abosen.xboot.sortableutil.infrastructure;

import lombok.RequiredArgsConstructor;
import top.abosen.xboot.sortableutil.domain.ExecuteMeta;
import top.abosen.xboot.sortableutil.domain.SortableElement;
import top.abosen.xboot.sortableutil.domain.SortableElementRepository;
import top.abosen.xboot.sortableutil.domain.SortableQuery;
import top.abosen.xboot.sortableutil.infrastructure.database.SortableElementMapper;
import top.abosen.xboot.sortableutil.infrastructure.database.SortedElementDto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author qiubaisen
 * @date 2021/4/30
 */
@SuppressWarnings("DuplicatedCode")
@RequiredArgsConstructor
public class SortableRepositoryImpl implements SortableElementRepository {
    private final SortableElementMapper mapper;


    @Override
    public void saveSortElements(ExecuteMeta executeMeta, List<SortableElement> elements) {

        List<SortedElementDto> modified = elements.stream()
                .filter(it -> it.getFlag().isModified())
                .map(it -> new SortedElementDto(
                        it.getId(),
                        it.getWeight(),
                        it.getStick(),
                        it.getRow()
                )).collect(Collectors.toList());

        if (modified.isEmpty()) {
            return;
        }
        mapper.saveElements(
                executeMeta.getTableName(),
                executeMeta.getIdField(),
                executeMeta.getWeightField(),
                executeMeta.getStickField(),
                executeMeta.getRowField(),
                executeMeta.getCondition(),
                modified);
    }

    @Override
    public long totalCount(ExecuteMeta executeMeta) {
        return mapper.totalCount(
                executeMeta.getTableName(),
                executeMeta.getCondition()
        );
    }

    @Override
    public List<SortableElement> query(SortableQuery query) {
        ExecuteMeta executeMeta = query.getExecuteMeta();
        boolean orderAsc = query.isOrderAsc();
        Long weightMin = query.getWeightMin();
        Long weightMax = query.getWeightMax();
        Long stickMin = query.getStickMin();
        Long stickMax = query.getStickMax();
        Long rowMin = query.getRowMin();
        Long rowMax = query.getRowMax();
        Long offset = query.getOffset();
        Long limit = query.getLimit();

        if ((offset != null && offset < 0) || (limit != null && limit <= 0) ||
                (weightMin != null && weightMax != null && weightMin > weightMax) ||
                (stickMin != null && stickMax != null && stickMin > stickMax) ||
                (rowMin != null && rowMax != null && rowMin > rowMax)
        ) {
            return Collections.emptyList();
        }
        return mapper.querySortElement(
                        executeMeta.getTableName(),
                        executeMeta.getIdField(),
                        executeMeta.getWeightField(),
                        executeMeta.getStickField(),
                        executeMeta.getRowField(),
                        executeMeta.getCondition(),
                        orderAsc, weightMin, weightMax,
                        stickMin, stickMax,
                        rowMin, rowMax,
                        offset, limit
                ).stream().map(it -> new SortableElement(it.getId(), it.getWeight(), it.getRow(), it.getStick()))
                .collect(Collectors.toList());
    }

    @Override
    public long count(SortableQuery query) {
        ExecuteMeta executeMeta = query.getExecuteMeta();
        boolean orderAsc = query.isOrderAsc();
        Long weightMin = query.getWeightMin();
        Long weightMax = query.getWeightMax();
        Long stickMin = query.getStickMin();
        Long stickMax = query.getStickMax();
        Long rowMin = query.getRowMin();
        Long rowMax = query.getRowMax();

        if ((weightMin != null && weightMax != null && weightMin > weightMax) ||
                (stickMin != null && stickMax != null && stickMin > stickMax) ||
                (rowMin != null && rowMax != null && rowMin > rowMax)
        ) {
            return 0L;
        }
        return mapper.countSortElement(
                executeMeta.getTableName(),
                executeMeta.getIdField(),
                executeMeta.getWeightField(),
                executeMeta.getStickField(),
                executeMeta.getRowField(),
                executeMeta.getCondition(),
                orderAsc, weightMin, weightMax,
                stickMin, stickMax,
                rowMin, rowMax
        );
    }

    @Override
    public SortableElement find(ExecuteMeta executeMeta, long idValue) {
        return Optional.ofNullable(
                mapper.findSortElement(
                        executeMeta.getTableName(),
                        executeMeta.getIdField(),
                        executeMeta.getWeightField(),
                        executeMeta.getStickField(),
                        executeMeta.getRowField(),
                        executeMeta.getCondition(),
                        idValue
                )
        ).map(it -> new SortableElement(it.getId(), it.getWeight(), it.getRow(), it.getStick())).orElse(null);
    }

}
