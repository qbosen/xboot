package top.abosen.toys.sortableapi.application;

import com.google.common.collect.Streams;
import lombok.RequiredArgsConstructor;
import top.abosen.toys.sortableapi.domain.ExecuteMeta;
import top.abosen.toys.sortableapi.domain.PagedList;
import top.abosen.toys.sortableapi.domain.SortableElement;
import top.abosen.toys.sortableapi.domain.SortableElementRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
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

    @Override public PagedList<SortableElement> query(ExecuteMeta executeMeta, long page, long size) {
        long total = sortableRepository.totalCount(executeMeta);
        // 不具备固定行的能力，直接查询并返回
        if (!executeMeta.hasRowFixAbility()) {
            List<SortableElement> data = sortableRepository.query(executeMeta,
                    false, null, null,
                    null, null, null,
                    (page - 1) * size, size);
            return new PagedList<>(total, data);
        }

        // 存在固定行元素
        return null;
    }


    @Override public boolean move(ExecuteMeta executeMeta, long id, int count) {
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
        List<SortableElement> mayChange = sortableRepository.query(executeMeta, !moveDown,
                /*权重降序排列，目标向上移动，目标权重为查询的最小值*/
                !moveDown ? target.getWeight() : null,
                /*权重降序排列，目标向下移动，目标权重为查询的最大值*/
                moveDown ? target.getWeight() : null,
                /*数据移动发生在非固定行数据中，发生在同种置顶状态的数据中*/
                0L, 0L, duringStick,
                /*移动count个元素，则影响了count+1调数据*/
                0L, Math.abs(count) + 1L);

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

    @Override public boolean moveToTop(ExecuteMeta executeMeta, long id) {
        SortableElement target = sortableRepository.find(executeMeta, id);
        if (target == null) {
            return false;
        }

        List<SortableElement> sortableElements = sortableRepository.query(executeMeta, false, null, null, 0L, 0L, target.isStick(), 0L, 1L);
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
        List<SortableElement> sortableElements = sortableRepository.query(executeMeta, false, min, nextMax, 0L, 0L, stick, null, null);
        return querySortableRecursive(executeMeta, sortableElements, stick, min, nextMax);
    }


    private boolean doSort(ExecuteMeta executeMeta, SortableElement target, boolean duringStick, int count) {
        if (count == 0) return false;
        boolean moveDown = count > 0;

        // 构建一种排序方式，这种方式使排序后的 目标一定是第一个元素。
        // 向下移动目标，目标权重减小，排序方式升序排列；
        // 向下移动目标，目标权重增大，排序方式降序排列；
        Comparator<SortableElement> comparator = moveDown ? weightAscThenIdAscSort : weightDescThenIdDescSort;

        // 再一次获取影响元素，这一次能够确保影响不扩散
        LinkedList<SortableElement> mayChange = sortableRepository.query(executeMeta, !moveDown,
                !moveDown ? target.getWeight() : null,
                moveDown ? target.getWeight() : null,
                0L, 0L, duringStick,
                0L, Math.abs(count) + 1L)
                .stream()
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

    @Override public boolean stick(ExecuteMeta executeMeta, long id, boolean stick) {
        SortableElement target = sortableRepository.find(executeMeta, id);
        if (target == null) {
            return false;
        }
        if (target.getRow() > 0) {
            // 固定行元素无法置顶
            return false;
        }
        if (target.isStick() == stick) {
            // 无需操作
            return false;
        }
        target.stick(stick);
        sortableRepository.saveSortElements(executeMeta, Collections.singletonList(target));
        return true;
    }

    @Override public boolean frozenRow(ExecuteMeta executeMeta, long id, long row, boolean frozen) {
        return false;
    }
}
