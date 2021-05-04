package top.abosen.toys.sortableapi.infrastructure;

import lombok.RequiredArgsConstructor;
import top.abosen.toys.sortableapi.domain.BaseMeta;
import top.abosen.toys.sortableapi.domain.ExecuteMeta;
import top.abosen.toys.sortableapi.domain.SortableElement;
import top.abosen.toys.sortableapi.domain.SortableElementRepository;
import top.abosen.toys.sortableapi.infrastructure.database.SortableElementDto;
import top.abosen.toys.sortableapi.infrastructure.database.SortableElementMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author qiubaisen
 * @date 2021/4/30
 */
@RequiredArgsConstructor
public class SortableRepositoryImpl implements SortableElementRepository {
    private final SortableElementMapper mapper;

    @Override
    public void saveSortElements(BaseMeta baseMeta, List<SortableElement> elements) {

        List<SortableElementDto> modified = elements.stream()
                .filter(it -> it.getFlag().isModified())
                .map(it -> SortableElementDto.of(
                        baseMeta.getTableName(),
                        baseMeta.getIdField(),
                        baseMeta.getSortField(),
                        it.getId(),
                        it.getWeight()
                )).collect(Collectors.toList());

        if (modified.isEmpty()) {
            return;
        }
        mapper.sortElements(modified);
    }

    @Override
    public List<SortableElement> query(ExecuteMeta executeMeta, boolean weightAsc, Long weightBegin, Long weightEnd, Long offset, Long limit) {
        if (weightBegin != null && weightEnd != null && weightBegin > weightEnd) {
            return Collections.emptyList();
        }
        return mapper.querySortElement(
                executeMeta.getBaseMeta().getTableName(),
                executeMeta.getBaseMeta().getIdField(),
                executeMeta.getBaseMeta().getSortField(),
                executeMeta.getCondition(),
                weightBegin,
                weightEnd,
                weightAsc,
                offset,
                limit
        ).stream()
                .map(it -> new SortableElement(it.getId(), it.getWeight()))
                .collect(Collectors.toList());
    }

    @Override
    public SortableElement find(ExecuteMeta executeMeta, long idValue) {
        return Optional.ofNullable(
                mapper.findSortElement(
                        executeMeta.getBaseMeta().getTableName(),
                        executeMeta.getBaseMeta().getIdField(),
                        executeMeta.getBaseMeta().getSortField(),
                        executeMeta.getCondition(),
                        idValue
                )
        ).map(it -> new SortableElement(it.getId(), it.getWeight()))
                .orElse(null);
    }

}
