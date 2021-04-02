package top.abosen.dddboot.shared.domain;


import top.abosen.dddboot.shared.event.DomainEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @author qiubaisen
 * @date 2021/3/31
 */
public abstract class BaseEntityAggregate<ID extends Serializable> extends BaseEntity<ID> implements Validatable {
    private List<DomainEvent> _events;

    protected final void raiseEvent(DomainEvent event) {
        get_events().add(event);
    }

    final void clearEvents() {
        get_events().clear();
    }

    final List<DomainEvent> get_events() {
        if (_events == null) {
            _events = new ArrayList<>();
        }
        return _events;
    }

}
