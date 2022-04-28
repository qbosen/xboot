package top.abosen.dddboot.sortableutil.domain;

import lombok.Value;

@Value
public class SortableQuery {
    ExecuteMeta executeMeta;

    boolean weightAsc;
    Long weightMin;
    Long weightMax;

    Long rowMin;
    Long rowMax;

    Boolean stick;

    Long offset;
    Long limit;


    public static Builder builder(ExecuteMeta executeMeta) {
        return new Builder(executeMeta);
    }

    public static final class Builder {
        ExecuteMeta executeMeta;
        boolean weightAsc;
        Long weightMin;
        Long weightMax;
        Long rowMin;
        Long rowMax;
        Boolean stick;
        Long offset;
        Long limit;

        private Builder(ExecuteMeta executeMeta) {
            this.executeMeta = executeMeta;
        }

        public Builder weightAsc(boolean weightAsc) {
            this.weightAsc = weightAsc;
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

        public Builder rowMin(Long rowMin) {
            this.rowMin = rowMin;
            return this;
        }

        public Builder rowMax(Long rowMax) {
            this.rowMax = rowMax;
            return this;
        }

        public Builder stick(Boolean stick) {
            this.stick = stick;
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

        public SortableQuery build() {
            return new SortableQuery(executeMeta, weightAsc, weightMin, weightMax, rowMin, rowMax, stick, offset, limit);
        }
    }
}
