package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.api.rest.response.category.CategoryListDataModel;
import hu.psprog.leaflet.lcfa.core.domain.content.CategorySummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Converts {@link CategoryListDataModel} to {@link List} of {@link CategorySummary} objects.
 *
 * @author Peter Smith
 */
@Component
public class CategorySummaryListConverter implements Converter<CategoryListDataModel, List<CategorySummary>> {

    private final CategorySummaryConverter categorySummaryConverter;

    @Autowired
    public CategorySummaryListConverter(CategorySummaryConverter categorySummaryConverter) {
        this.categorySummaryConverter = categorySummaryConverter;
    }

    @Override
    public List<CategorySummary> convert(CategoryListDataModel source) {
        return source.categories().stream()
                .map(categorySummaryConverter::convert)
                .collect(Collectors.toList());
    }
}
