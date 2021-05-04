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

    public void update(long weight) {
        if (this.weight == weight) {
            flag.notModify();
        } else {
            this.weight = weight;
            flag.modify();
        }
    }

    @Override
    public String toString() {
        return String.format("{%d: %d}", id, weight);
    }
}
