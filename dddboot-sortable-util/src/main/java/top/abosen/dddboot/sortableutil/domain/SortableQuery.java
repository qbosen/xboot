package top.abosen.dddboot.sortableutil.domain;

import lombok.Value;

import java.util.function.Consumer;
import java.util.function.Function;

@Value
public class SortableQuery {
    ExecuteMeta executeMeta;

    boolean orderAsc;
    Long weightMin;
    Long weightMax;

    Long rowMin;
    Long rowMax;

    Long stickMin;
    Long stickMax;

    Long offset;
    Long limit;


    public static Builder builder(ExecuteMeta executeMeta) {
        return new Builder(executeMeta);
    }

    public static final class Builder {
        ExecuteMeta executeMeta;
        boolean orderAsc;
        Long weightMin;
        Long weightMax;
        Long rowMin;
        Long rowMax;
        Long stickMin;
        Long stickMax;
        Long offset;
        Long limit;

        private Builder(ExecuteMeta executeMeta) {
            this.executeMeta = executeMeta;
        }

        public Builder orderAsc(boolean orderAsc) {
            this.orderAsc = orderAsc;
            return this;
        }

        public Builder weightMin(Long weightMin) {
            this.weightMin = weightMin;
            return this;
        }

        public Builder weightMax(Long weightMax) {
            this.weightMax = weightMax;
            return this;
        }

        public Builder row(boolean frozen) {
            rowMin = frozen ? 1L : 0L;
            rowMax = frozen ? null : 0L;
            return this;
        }

        public Builder rowMin(Long rowMin) {
            this.rowMin = rowMin;
            return this;
        }

        public Builder rowMax(Long rowMax) {
            this.rowMax = rowMax;
            return this;
        }

        public Builder stick(boolean stick) {
            stickMin = stick ? 1L : 0L;
            stickMax = stick ? null : 0L;
            return this;
        }

        public Builder stickMin(Long stickMin) {
            this.stickMin = stickMin;
            return this;
        }

        public Builder stickMax(Long stickMax) {
            this.stickMax = stickMax;
            return this;
        }

        public Builder offset(Long offset) {
            this.offset = offset;
            return this;
        }

        public Builder limit(Long limit) {
            this.limit = limit;
            return this;
        }

        public Builder condition(boolean condition, Consumer<Builder> consumer) {
            if (condition) {
                consumer.accept(this);
            }
            return this;
        }

        public SortableQuery build() {
            return new SortableQuery(executeMeta, orderAsc, weightMin, weightMax, rowMin, rowMax, stickMin, stickMax, offset, limit);
        }
    }
}
