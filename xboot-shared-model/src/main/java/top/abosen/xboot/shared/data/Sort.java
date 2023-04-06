package top.abosen.xboot.shared.data;


import top.abosen.xboot.shared.data.support.DefaultSort;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author qiubaisen
 * @since 2021/3/31
 */
public interface Sort {

    static Sort unsorted() {
        return new DefaultSort();
    }

    static Sort by(Direction direction, String... properties) {
        if (direction == null) {
            direction = Direction.ASC;
        }
        Objects.requireNonNull(properties, "Properties must not be null!");

        Sort sort = Sort.unsorted();
        for (String property : properties) {
            sort = direction.isAscending() ? sort.asc(property) : sort.desc(property);
        }
        return sort;
    }

    Sort asc(String property);

    Sort desc(String property);

    List<Order> getOrders();

    enum Direction {
        ASC, DESC;

        public boolean isAscending() {
            return this.equals(ASC);
        }

        public boolean isDescending() {
            return this.equals(DESC);
        }
    }

    interface Order extends Serializable {

        String getProperty();

        void setProperty(String property);

        Direction getDirection();

        boolean isAscending();

        boolean isDescending();
    }

}
