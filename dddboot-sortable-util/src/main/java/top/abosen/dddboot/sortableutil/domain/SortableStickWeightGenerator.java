package top.abosen.dddboot.sortableutil.domain;

public interface SortableStickWeightGenerator {
    /**
     * 每次置顶时,设置一个默认的置顶权重,用于置顶内容间排序;
     * <p>
     * 此方法值应该随时间递增: 后置顶的权重 > 先置顶的权重
     * <p>
     * 此方法返回值 应该大于置顶数据总数: 避免移动后,产生0/负数的权重
     *
     * @return 置顶权重值
     */
    long nextWeight();
}
