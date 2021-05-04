package top.abosen.toys.sortableapi.application;

import com.google.common.collect.Streams;
import lombok.RequiredArgsConstructor;
import top.abosen.toys.sortableapi.domain.ExecuteMeta;
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

    @Override public List<SortableElement> query(ExecuteMeta executeMeta, long page, long size) {
        return sortableRepository.query(executeMeta, false, null, null, (page - 1) * size, size);
    }

    @Override public boolean move(ExecuteMeta executeMeta, long id, int count) {
        if (count == 0) {
            return false;
        }
        SortableElement target = sortableRepository.find(executeMeta, id);
        if (target == null) {
            return false;
        }
        boolean moveDown = count > 0;

        List<SortableElement> mayChange = sortableRepository.query(executeMeta, !moveDown,
                moveDown ? null : target.getWeight(),
                moveDown ? target.getWeight() : null,
                0L, Math.abs(count) + 1L);
        long min = mayChange.stream().mapToLong(SortableElement::getWeight).min().orElse(target.getWeight());
        long max = mayChange.stream().mapToLong(SortableElement::getWeight).max().orElse(target.getWeight());

        List<SortableElement> elements = querySortableRecursive(executeMeta, Collections.singletonList(target), min, max)
                .stream().sorted(Comparator.comparing(SortableElement::getWeight).reversed()
                        .thenComparing(Comparator.comparing(SortableElement::getId).reversed()))
                .collect(Collectors.toList());

        if (elements.size() > 1) {
            // 规整化 确保target的上下是单调的
            long baseWeight = elements.get(0).getWeight();

            sortableRepository.saveSortElements(executeMeta.getBaseMeta(),
                    Streams.mapWithIndex(elements.stream(), (element, idx) -> {
                        element.update(baseWeight - idx);
                        if (element.getId() == target.getId()) target.update(baseWeight - idx);
                        return element;
                    }).collect(Collectors.toList())
            );
        }

        return doSort(executeMeta, target, count);
    }

    @Override public boolean moveToTop(ExecuteMeta executeMeta, long id) {
        List<SortableElement> sortableElements = sortableRepository.query(executeMeta, false, null, null, 0L, 1L);
        if (sortableElements.isEmpty()) {
            return false;
        }
        SortableElement topElement = sortableElements.get(0);
        SortableElement toSave = new SortableElement();
        toSave.setId(id);
        toSave.setWeight(topElement.getWeight() + 1);
        toSave.getFlag().modify();

        sortableRepository.saveSortElements(executeMeta.getBaseMeta(), Collections.singletonList(toSave));
        return true;
    }


    /**
     * 对于指定的range找出并扩充可能被影响的内容
     */
    private List<SortableElement> querySortableRecursive(ExecuteMeta executeMeta, List<SortableElement> elements, long begin, long end) {
        long nextEnd = end + elements.size() - 1;
        List<SortableElement> sortableElements = sortableRepository.query(executeMeta, false, begin, nextEnd, null, null);
        if (elements.size() == sortableElements.size()) return sortableElements;

        return querySortableRecursive(executeMeta, sortableElements, begin, end);
    }


    private boolean doSort(ExecuteMeta executeMeta, SortableElement target, int count) {
        if (count == 0) return false;
        boolean moveDown = count > 0;

        //目标一定是第一个元素。 向下移动，升序排列；向上移动，降序排列
        Comparator<SortableElement> comparator = moveDown ?
                Comparator.comparing(SortableElement::getWeight).thenComparing(SortableElement::getId) :
                Comparator.comparing(SortableElement::getWeight).reversed().thenComparing(Comparator.comparing(SortableElement::getId).reversed());

        Long min = moveDown ? null : target.getWeight();
        Long max = moveDown ? target.getWeight() : null;
        LinkedList<SortableElement> mayChange = sortableRepository.query(executeMeta, !moveDown,
                min, max,
                0L, Math.abs(count) + 1L)
                .stream()
                .filter(it -> it.getId() != target.getId())     // 过滤掉目标
                .sorted(comparator)
                .collect(Collectors.toCollection(LinkedList::new));

        if (mayChange.isEmpty()) return false;

        target.update(mayChange.get(0).getWeight());
        mayChange.addFirst(target);

        SortableElement prev = null;
        for (SortableElement element : mayChange) {
            if (prev != null && (moveDown ?
                    element.getWeight() <= prev.getWeight() :
                    element.getWeight() >= prev.getWeight())) {
                element.update(element.getWeight() + (moveDown ? 1 : -1));
            }
            prev = element;
        }

        sortableRepository.saveSortElements(executeMeta.getBaseMeta(), mayChange);
        return true;
    }
}
