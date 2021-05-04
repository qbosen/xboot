package top.abosen.toys.sortableapi.domain;

import lombok.Value;

/**
 * @author qiubaisen
 * @date 2021/4/30
 */

@Value
public class ExecuteMeta {
    BaseMeta baseMeta;
    String condition;

    public static ExecuteMetaBuilder builder(BaseMeta baseMeta) {
        return new ExecuteMetaBuilder(baseMeta);
    }

    public static final class ExecuteMetaBuilder {
        private final BaseMeta baseMeta;
        private String condition;

        private ExecuteMetaBuilder(BaseMeta baseMeta) {
            this.baseMeta = baseMeta;
        }


        public ExecuteMetaBuilder condition(String condition) {
            this.condition = condition;
            return this;
        }

        public ExecuteMeta build() {
            return new ExecuteMeta(this.baseMeta, this.condition);
        }
    }
}
