package top.abosen.xboot.sortableutil.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qiubaisen
 * @since 2021/4/30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SortableElement {
    private final Flag flag = new Flag();

    private long id;
    private long weight;
    private long row;
    private long stick;

    public boolean setSort(long weight) {
        if (isStick()) {
            return stick(weight);
        } else {
            return weight(weight);
        }
    }

    public long getSort() {
        if (isStick()) {
            return stick;
        } else {
            return weight;
        }
    }

    public boolean weight(long weight) {
        if (this.weight == weight) {
            flag.notModify();
            return false;
        } else {
            this.weight = weight;
            flag.modify();
            return true;
        }
    }

    public boolean isStick() {
        return stick > 0;
    }

    public boolean unstick() {
        return stick(0);
    }

    public boolean stick(long stick) {
        if (stick < 0) {
            stick = 0;
        }
        if (this.stick == stick) {
            flag.notModify();
            return false;
        } else {
            this.stick = stick;
            flag.modify();
            return true;
        }
    }

    public boolean isFrozen() {
        return this.row > 0;
    }

    public boolean unfrozen() {
        return frozen(0);
    }

    /**
     * 固定行
     *
     * @param row 0表示不固定行
     */
    public boolean frozen(long row) {
        if (row < 0) {
            row = 0;
        }
        if (this.row == row) {
            flag.notModify();
            return false;
        } else {
            this.row = row;
            flag.modify();
            return true;
        }
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{").append("id:").append(id)
                .append(", w:").append(weight);
        if (row != 0) {
            builder.append(", r:").append(row);
        }
        if (stick != 0) {
            builder.append(", s:").append(stick);
        }
        return builder.append('}').toString();
    }
}
