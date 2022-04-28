package top.abosen.dddboot.sortableutil.application;

import com.google.common.collect.Streams;
import lombok.RequiredArgsConstructor;
import top.abosen.dddboot.sortableutil.domain.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author qiubaisen
 * @date 2021/5/4
 */
@SuppressWarnings("UnstableApiUsage")
@RequiredArgsConstructor
public class SortableCommonServiceImpl implements SortableCommonService {
    private final SortableElementRepository sortableRepository;

    // 权重降序 >> id降序
    private final Comparator<SortableElement> weightDescThenIdDescSort = Comparator.comparing(SortableElement::getWeight).reversed()
            .thenComparing(Comparator.comparing(SortableElement::getId).reversed());
    // 权重升序 >> id升序
    private final Comparator<SortableElement> weightAscThenIdAscSort = Comparator.comparing(SortableElement::getWeight)
            .thenComparing(SortableElement::getId);

    @Override
    public PagedList<SortableElement> query(ExecuteMeta executeMeta, long page, long size) {
        long total = sortableRepository.totalCount(executeMeta);
        long offset = (page - 1) * size;
        // 不具备固定行的能力，直接查询并返回
        if (!executeMeta.hasRowFixAbility()) {
            List<SortableElement> data = sortableRepository.query(
                    SortableQuery.builder(executeMeta).offset(offset).limit(size).build()
            );
            return new PagedList<>(total, data);
        }

        // 存在固定行元素
        // 1. 所求范围的固定元素
        List<SortableElement> frozenData = sortableRepository.query(
                SortableQuery.builder(executeMeta).rowMin(offset + 1).rowMax(offset + size).stick(false).build()
        );
        // 2. 之前已经固定的行数
        long frozenBefore = offset == 0 ? 0 : sortableRepository.count(
                SortableQuery.builder(executeMeta).rowMin(1L).rowMax(offset).stick(false).build()
        );
        // 3. 需要填充的非固定内容
        List<SortableElement> unfrozenData = frozenData.size() == size ? Collections.emptyList() : sortableRepository.query(
                SortableQuery.builder(executeMeta).rowMin(0L).rowMax(0L).offset(offset - frozenBefore).limit(size - frozenData.size()).build()
        );
        // 4. 合并
        frozenData.sort(Comparator.comparingLong(SortableElement::getRow));

        List<SortableElement> data = new ArrayList<>(unfrozenData);
        for (SortableElement element : frozenData) {
            int index = (int) (element.getRow() - offset - 1);
            if (index > data.size()) {
                index = data.size();
            }
            data.add(index, element);
        }
        // 5. 数据不足, 填充超出范围的固定行数据
        if (data.size() < size) {
            List<SortableElement> outsideFrozen = sortableRepository.query(
                    SortableQuery.builder(executeMeta).rowMin(offset + 1).offset(0L).limit(size - data.size()).build()
            );
            outsideFrozen.sort(Comparator.comparingLong(SortableElement::getRow));
            data.addAll(outsideFrozen);
        }

        return new PagedList<>(total, data);
    }


    @Override
    public boolean move(ExecuteMeta executeMeta, long id, int count) {
        if (count == 0) {
            return false;
        }
        SortableElement target = sortableRepository.find(executeMeta, id);
        if (target == null) {
            return false;
        }
        if (target.getRow() > 0) {
            // 固定行元素无法移动
            return false;
        }
        boolean moveDown = count > 0;
        // 置顶元素的移动在 置顶元素内发生
        boolean duringStick = target.isStick();

        // 原始排序一定是 置顶>权重降序>id降序
        List<SortableElement> mayChange = sortableRepository.query(
                SortableQuery.builder(executeMeta)
                        .weightAsc(!moveDown)
                        /*权重降序排列，目标向上移动，目标权重为查询的最小值*/
                        .weightMin(!moveDown ? target.getWeight() : null)
                        /*权重降序排列，目标向下移动，目标权重为查询的最大值*/
                        .weightMax(moveDown ? target.getWeight() : null)
                        .rowMin(0L).rowMax(0L)
                        /*数据移动发生在非固定行数据中，发生在同种置顶状态的数据中*/
                        .stick(duringStick)
                        .offset(0L)
                        /*移动count个元素，则影响了count+1条数据*/
                        .limit(Math.abs(count) + 1L)
                        .build()
        );

        // 计算权重槽是否充足
        long min = mayChange.stream().mapToLong(SortableElement::getWeight).min().orElse(target.getWeight());
        long max = mayChange.stream().mapToLong(SortableElement::getWeight).max().orElse(target.getWeight());

        // 受影响的数据：满足权重槽位数量、按权重降序&id降序
        List<SortableElement> elements = querySortableRecursive(executeMeta, mayChange, duringStick, min, max)
                .stream().sorted(weightDescThenIdDescSort).collect(Collectors.toList());

        // 规整化受影响的数据
        if (elements.size() > 1) {
            long baseWeight = elements.get(0).getWeight();

            sortableRepository.saveSortElements(executeMeta,
                    Streams.mapWithIndex(elements.stream(), (element, idx) -> {
                        element.update(baseWeight - idx);
                        if (element.getId() == target.getId()) {
                            /*内存中的target与数据库中保持一直*/
                            target.update(baseWeight - idx);
                        }
                        return element;
                    }).collect(Collectors.toList())
            );
        }

        return doSort(executeMeta, target, duringStick, count);
    }

    @Override
    public boolean moveToTop(ExecuteMeta executeMeta, long id) {
        SortableElement target = sortableRepository.find(executeMeta, id);
        if (target == null) {
            return false;
        }

        List<SortableElement> sortableElements = sortableRepository.query(
                SortableQuery.builder(executeMeta)
                        .rowMin(0L).rowMax(0L)
                        .stick(target.isStick())
                        .offset(0L).limit(1L)
                        .build()
        );
        if (sortableElements.isEmpty()) {
            return false;
        }
        SortableElement top = sortableElements.get(0);
        if (top.getId() == target.getId()) {
            // 已经是最上面的元素
            return false;
        }
        target.update(top.getWeight() + 1);

        sortableRepository.saveSortElements(executeMeta, Collections.singletonList(target));
        return true;
    }


    /**
     * 递归，可能存在性能问题，权重尽量稀疏
     * 对于指定的range找出并扩充可能被影响的内容
     */
    private List<SortableElement> querySortableRecursive(ExecuteMeta executeMeta, List<SortableElement> elements,
                                                         boolean stick, long min, long max) {
        // 当前权重槽位数量 满足数据重排需要
        if (max - min + 1 >= elements.size()) {
            return elements;
        }
        long nextMax = min + elements.size() - 1;
        List<SortableElement> sortableElements = sortableRepository.query(
                SortableQuery.builder(executeMeta)
                        .weightMin(min).weightMax(nextMax)
                        .rowMin(0L).rowMax(0L)
                        .stick(stick)
                        .build()
        );
        return querySortableRecursive(executeMeta, sortableElements, stick, min, nextMax);
    }


    private boolean doSort(ExecuteMeta executeMeta, SortableElement target, boolean duringStick, int count) {
        if (count == 0) {
            return false;
        }

        boolean moveDown = count > 0;

        // 构建一种排序方式，这种方式使排序后的 目标一定是第一个元素。
        // 向下移动目标，目标权重减小，排序方式升序排列；
        // 向下移动目标，目标权重增大，排序方式降序排列；
        Comparator<SortableElement> comparator = moveDown ? weightAscThenIdAscSort : weightDescThenIdDescSort;

        // 再一次获取影响元素，这一次能够确保影响不扩散
        LinkedList<SortableElement> mayChange = sortableRepository.query(
                        SortableQuery.builder(executeMeta).weightAsc(!moveDown)
                                .weightMin(!moveDown ? target.getWeight() : null)
                                .weightMax(moveDown ? target.getWeight() : null)
                                .rowMin(0L).rowMax(0L)
                                .stick(duringStick)
                                .offset(0L).limit(Math.abs(count) + 1L)
                                .build()
                ).stream()
                .filter(it -> it.getId() != target.getId())     // 过滤掉目标，因为目标不可能是第一个元素
                .sorted(comparator)
                .collect(Collectors.toCollection(LinkedList::new));

        if (mayChange.isEmpty()) {
            return false;
        }

        // 目标位于此排列的第一个，目标权重替换为此排列的第一个元素
        target.update(mayChange.get(0).getWeight());
        mayChange.addFirst(target);

        // 只进行必要的更新。当权重稀疏的时候 减少需要更新的数据个数
        SortableElement prev = null;
        for (SortableElement element : mayChange) {
            if (prev != null && (moveDown ?
                    element.getWeight() <= prev.getWeight() :
                    element.getWeight() >= prev.getWeight())) {
                element.update(element.getWeight() + (moveDown ? 1 : -1));
            }
            prev = element;
        }

        sortableRepository.saveSortElements(executeMeta, mayChange);
        return true;
    }

    @Override
    public boolean stick(ExecuteMeta executeMeta, long id, boolean stick) {
        SortableElement target = sortableRepository.find(executeMeta, id);
        if (target == null) {
            return false;
        }
        if (target.getRow() > 0) {
            // 固定行元素无法置顶
            return false;
        }
        if (!target.stick(stick)) {
            return false;
        }

        sortableRepository.saveSortElements(executeMeta, Collections.singletonList(target));
        return true;
    }

    @Override
    public boolean frozenRow(ExecuteMeta executeMeta, long id, long row, boolean override) {
        SortableElement target = sortableRepository.find(executeMeta, id);
        if (target == null) {
            return false;
        }
        if (target.isStick()) {
            return false;
        }
        // 取消固定行
        if (row <= 0) {
            if (!target.frozen(0)) {
                return false;
            }
            sortableRepository.saveSortElements(executeMeta, Collections.singletonList(target));
            return true;
        }

        // 设置固定行; row>0
        List<SortableElement> toSave = new ArrayList<>();
        List<SortableElement> previousFrozen = sortableRepository.query(
                SortableQuery.builder(executeMeta).rowMin(row).rowMax(row).stick(false).build()
        );

        if (!previousFrozen.isEmpty()) {
            // 非覆盖模式,放弃覆盖
            if (!override) {
                return false;
            }
            // 覆盖模式,修改所有非目标的旧数据
            previousFrozen.stream().filter(it -> it.getId() != target.getId()).forEach(it -> it.frozen(0));
            toSave.addAll(previousFrozen);
        }

        if (target.frozen(row)) {
            toSave.add(target);
        }

        if (toSave.isEmpty()) {
            return false;
        }
        sortableRepository.saveSortElements(executeMeta, toSave);
        return true;
    }
}
