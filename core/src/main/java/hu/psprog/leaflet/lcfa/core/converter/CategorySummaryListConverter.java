package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.api.rest.response.category.CategoryDataModel;
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

    private LinkAliasGenerator linkAliasGenerator;

    @Autowired
    public CategorySummaryListConverter(LinkAliasGenerator linkAliasGenerator) {
        this.linkAliasGenerator = linkAliasGenerator;
    }

    @Override
    public List<CategorySummary> convert(CategoryListDataModel source) {
        return source.getCategories().stream()
                .map(this::createCategorySummary)
                .collect(Collectors.toList());
    }

    private CategorySummary createCategorySummary(CategoryDataModel categoryDataModel) {
        return CategorySummary.builder()
                .id(categoryDataModel.getId())
                .title(categoryDataModel.getTitle())
                .alias(linkAliasGenerator.generateAlias(categoryDataModel.getTitle()))
                .build();
    }
}
