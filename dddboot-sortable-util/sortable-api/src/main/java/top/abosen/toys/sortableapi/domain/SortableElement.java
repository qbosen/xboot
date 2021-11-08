package top.abosen.toys.sortableapi.domain;

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

    public void stick(boolean stick){
        if (this.stick == stick) {
            flag.notModify();
        }else{
            this.stick = stick;
            flag.modify();
        }
    }

    @Override public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{").append("id:").append(id)
                .append(", w:").append(weight);
        if (row != 0) {
            builder.append(", r:").append(row);
        }
        if (stick) {
            builder.append(", sticky");
        }
        return builder.append('}').toString();
    }
}
