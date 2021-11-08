package top.abosen.dddboot.shared.event;

import lombok.Getter;
import top.abosen.dddboot.shared.utility.UuidHelper;

import java.time.Instant;

import static java.time.Instant.now;


@Getter
public abstract class DomainEvent {
    private final String _id = UuidHelper.generateUuid(UuidHelper.Type.DIGITS);
    private final Instant _createdAt = now();

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[" + _id + "]";
    }

}
