package top.abosen.dddboot.sortableutil.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qiubaisen
 * @date 2021/4/30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SortableElement {
    private final Flag flag = new Flag();

    private long id;
    private long weight;
    private long row;
    private boolean stick;

    public void update(long weight) {
        if (this.weight == weight) {
            flag.notModify();
        } else {
            this.weight = weight;
            flag.modify();
        }
    }

    public boolean stick(boolean stick) {
        if (this.stick == stick) {
            flag.notModify();
            return false;
        } else {
            this.stick = stick;
            flag.modify();
            return true;
        }
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
        if (stick) {
            builder.append(", s");
        }
        return builder.append('}').toString();
    }
}
