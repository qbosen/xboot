package top.abosen.xboot.spring.repository.mybatisplus;

import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;
import top.abosen.xboot.shared.domain.PersistFlag;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.stream.Collectors.*;

@UtilityClass
public class PersistFlagRepositoryHelper {

    public static <DO extends PersistFlag, PO, ID extends Serializable> List<PO> statefulHandle(
            List<DO> rawData, Function<DO, PO> dataMapper, Function<PO, ID> idMapper,
            Consumer<List<ID>> deleter,
            Consumer<List<PO>> creator,
            Consumer<PO> modifier
    ) {
        Map<PersistFlag.PersistType, List<PO>> stateMap = rawData.stream()
                .collect(groupingBy(PersistFlag::getPersistType, mapping(dataMapper, toList())));
        Optional.ofNullable(stateMap.get(PersistFlag.PersistType.DELETED))
                .map(it -> it.stream().map(idMapper).collect(toList()))
                .filter(CollectionUtils::isNotEmpty)
                .ifPresent(deleter);
        Optional.ofNullable(stateMap.get(PersistFlag.PersistType.CREATED))
                .filter(CollectionUtils::isNotEmpty)
                .ifPresent(creator);
        Optional.ofNullable(stateMap.get(PersistFlag.PersistType.MODIFIED))
                .filter(CollectionUtils::isNotEmpty)
                .ifPresent(it -> it.forEach(modifier));
        return stateMap.entrySet().stream().filter(it -> !it.getKey().isDeleted())
                .flatMap(it -> it.getValue().stream())
                .collect(toList());
    }

    public static <DO extends PersistFlag, PO, ID extends Serializable> List<PO> statefulHandle(
            List<DO> rawData, Function<DO, PO> dataMapper, Function<PO, ID> idMapper,
            BaseMapper<PO> baseMapper
    ) {
        return statefulHandle(rawData, dataMapper, idMapper,
                baseMapper::deleteBatchIds, baseMapper::insertBatchSomeColumn, baseMapper::updateById);
    }
}