package top.abosen.dddboot.sortableutil.domain;

public class SortableStickWeightDefaultGenerator implements SortableStickWeightGenerator {
    @Override
    public long nextWeight() {
        return System.currentTimeMillis();
    }
}
