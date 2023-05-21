package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.api.rest.response.category.CategoryDataModel;
import hu.psprog.leaflet.lcfa.core.domain.content.CategorySummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts a {@link CategoryDataModel} object to {@link CategorySummary}.
 *
 * @author Peter Smith
 */
@Component
public class CategorySummaryConverter implements Converter<CategoryDataModel, CategorySummary> {

    private final LinkAliasGenerator linkAliasGenerator;

    @Autowired
    public CategorySummaryConverter(LinkAliasGenerator linkAliasGenerator) {
        this.linkAliasGenerator = linkAliasGenerator;
    }

    @Override
    public CategorySummary convert(CategoryDataModel source) {
        return CategorySummary.builder()
                .id(source.id())
                .title(source.title())
                .alias(linkAliasGenerator.generateAlias(source.title()))
                .build();
    }
}
