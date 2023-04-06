package top.abosen.xboot.spring.data;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import top.abosen.xboot.shared.data.Query;

import java.util.stream.Collectors;

/**
 * @author qiubaisen
 * @since 2021/3/31
 */
public abstract class QueryUtils {
    public static <T> com.baomidou.mybatisplus.core.metadata.IPage<T> ipage(Query query) {
        Page<T> page = new Page<>(query.getPage(), query.getSize());
        page.addOrder(
                query.getSort().getOrders().stream()
                        .map(it -> it.isAscending() ? OrderItem.asc(it.getProperty()) : OrderItem.desc(it.getProperty()))
                        .collect(Collectors.toList()));
        return page;
    }

    public static org.springframework.data.domain.Pageable pageable(Query query) {
        return PageRequest.of(query.getPage() - 1, query.getSize(),
                Sort.by(query.getSort().getOrders().stream()
                        .map(it -> it.isAscending() ? Sort.Order.asc(it.getProperty()) : Sort.Order.desc(it.getProperty()))
                        .collect(Collectors.toList())));
    }
}
