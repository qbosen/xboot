package top.abosen.xboot.shared.data.support;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.junit.jupiter.api.Test;
import top.abosen.xboot.shared.data.Pageable;
import top.abosen.xboot.shared.utility.UuidHelper;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author qiubaisen
 * @since 2023/3/10
 */
class QuerySupportTest {

    @SuperBuilder
    @Getter
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    static class SimpleQuery extends QuerySupport {
        final Long id;
    }

    @Test
    void should_super_build_without_default_constructor() {
        SimpleQuery query = SimpleQuery.builder().id(1L).page(3).build();
        assertThat(query.getId()).isEqualTo(1L);
        assertThat(query.getPage()).isEqualTo(3);
        assertThat(query.getSize()).isEqualTo(Pageable.DEFAULT_SIZE);
        assertThat(query.getSort()).isEqualTo(new DefaultSort());
    }

    @SuperBuilder
    @Getter
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    static class ComplexQuery extends QuerySupport{
        @Singular
        Set<Long> ids;
        @Builder.Default
        UuidHelper.Type type = UuidHelper.Type.DIGITS;
    }

    @Test
    void should_parse_singular() {
        ComplexQuery query = ComplexQuery.builder().id(1L).id(2L).ids(Arrays.asList(7L, 8L)).build();
        assertThat(query.getIds()).hasSize(4);
        assertThat(query.getType()).isEqualTo(UuidHelper.Type.DIGITS);
    }
}