package top.abosen.xboot.objectdiffer;

import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.category.CategoryService;
import de.danielbechler.diff.circular.CircularReferenceService;
import de.danielbechler.diff.comparison.ComparisonService;
import de.danielbechler.diff.differ.*;
import de.danielbechler.diff.filtering.ReturnableNodeService;
import de.danielbechler.diff.identity.IdentityService;
import de.danielbechler.diff.inclusion.InclusionService;
import de.danielbechler.diff.introspection.IntrospectionService;

/**
 * @author qiubaisen
 * @date 2023/1/15
 */

public class ObjectDiffer {
    final de.danielbechler.diff.ObjectDiffer objectDiffer;
    final DiffFormatter formatter;
    final ValueProviders valueProviders;

    private ObjectDiffer(de.danielbechler.diff.ObjectDiffer objectDiffer, DiffFormatter formatter, ValueProviders valueProviders) {
        this.objectDiffer = objectDiffer;
        this.formatter = formatter;
        this.valueProviders = valueProviders;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static ObjectDiffer buildDefault() {
        return builder().build();
    }

    static class Builder {
        final de.danielbechler.diff.ObjectDiffer objectDiffer;
        final DiffFormatter diffFormatter;
        final ValueProviders valueProviders;

        public Builder() {
            objectDiffer = defaultObjectDiffer();
            valueProviders = defaultFormatSource();
            diffFormatter = defaultDiffFormatter(valueProviders);
        }

        public Builder registerProvider(ValueProvider provider) {
            valueProviders.register(provider);
            return this;
        }

        public ObjectDiffer build() {
            return new ObjectDiffer(objectDiffer, diffFormatter, valueProviders);
        }

        private static ValueProviders defaultFormatSource() {
            return new ValueProviders();
        }

        public static DiffFormatter defaultDiffFormatter(ValueProviders valueProviders) {
            return new TemplatedDiffFormatter(defaultFormatterConfiguration(), valueProviders);
        }

        private static DiffFormatterConfiguration defaultFormatterConfiguration() {
            return DiffFormatterConfiguration.builder().build();
        }

        protected static de.danielbechler.diff.ObjectDiffer defaultObjectDiffer() {
            ObjectDifferBuilder dummy = ObjectDifferBuilder.startBuilding();
            DifferProvider differProvider = new DifferProvider();
            IntrospectionService introspectionService = new IntrospectionService(dummy);
            CategoryService categoryService = new CategoryService(dummy);
            InclusionService inclusionService = new InclusionService(categoryService, dummy);
            ComparisonService comparisonService = new DelegateComparisonService(dummy);
            IdentityService identityService = new DelegateIdentityService(dummy);
            ReturnableNodeService returnableNodeService = new ReturnableNodeService(dummy);
            CircularReferenceService circularReferenceService = new CircularReferenceService(dummy);
            final DifferDispatcher differDispatcher = new DifferDispatcher(differProvider, circularReferenceService, circularReferenceService, inclusionService, returnableNodeService, introspectionService, categoryService);

            inclusionService.resolveUsing(new DifferAnnotationInclusionResolver());
            differProvider.push(new BeanDiffer(differDispatcher, introspectionService, returnableNodeService, comparisonService, introspectionService));
            differProvider.push(new CollectionDiffer(differDispatcher, comparisonService, identityService));
            differProvider.push(new ArrayDiffer(differDispatcher, comparisonService, identityService));
            differProvider.push(new MapDiffer(differDispatcher, comparisonService));
            differProvider.push(new PrimitiveDiffer(comparisonService));
            return new de.danielbechler.diff.ObjectDiffer(differDispatcher);

        }


    }

    public String compare(Object source, Object target) {
        DiffVisitor visitor = new DiffVisitor(source, target);
        objectDiffer.compare(target, source).visit(visitor);
        return formatter.format(visitor.getDifferences());
    }


}
