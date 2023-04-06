package top.abosen.xboot.shared.data.support;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.abosen.xboot.shared.data.Sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author qiubaisen
 * @since 2021/3/31
 */
@EqualsAndHashCode
public class DefaultSort implements Sort {
    private final List<Order> orders = new ArrayList<>();

    @Override
    public Sort asc(String property) {
        this.orders.add(new DefaultOrder(property, Direction.ASC));
        return this;
    }

    @Override
    public Sort desc(String property) {
        this.orders.add(new DefaultOrder(property, Direction.DESC));
        return this;
    }

    @Override
    public List<Order> getOrders() {
        return Collections.unmodifiableList(orders);
    }

    @Override
    public String toString() {
        return this.orders.stream().map(Object::toString).collect(Collectors.joining(","));
    }

    @Data
    private static class DefaultOrder implements Sort.Order {

        private final Direction direction;
        private String property;

        DefaultOrder(String property, Direction direction) {
            this.property = property;
            this.direction = direction;
        }

        @Override
        public String toString() {
            return String.format("%s %s", property, direction);
        }

        @Override
        public boolean isAscending() {
            return Direction.ASC.equals(this.direction);
        }

        @Override
        public boolean isDescending() {
            return Direction.DESC.equals(this.direction);
        }
    }
}
