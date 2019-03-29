package hu.psprog.leaflet.lcfa.web.ui.support.navigation.impl;

import hu.psprog.leaflet.lcfa.core.domain.content.CategorySummary;
import hu.psprog.leaflet.lcfa.web.controller.BaseController;
import hu.psprog.leaflet.lcfa.web.model.NavigationItem;
import hu.psprog.leaflet.lcfa.web.ui.support.navigation.NavigationItemFactory;
import org.springframework.stereotype.Component;

/**
 * {@link NavigationItemFactory} implementation for generating navigation bar items based on {@link CategorySummary} objects.
 *
 * @author Peter Smith
 */
@Component
public class CategorySummaryNavigationItemFactory implements NavigationItemFactory<CategorySummary> {

    private static final String CATEGORY_ID = "{categoryID}";
    private static final String CATEGORY_ALIAS = "{categoryAlias}";

    @Override
    public NavigationItem create(CategorySummary sourceModel) {
        return NavigationItem.build(createLink(sourceModel), sourceModel.getTitle());
    }

    @Override
    public Class<CategorySummary> forModelClass() {
        return CategorySummary.class;
    }

    private String createLink(CategorySummary categorySummary) {
        return BaseController.PATH_FILTER_BY_CATEGORY
                .replace(CATEGORY_ID, String.valueOf(categorySummary.getId()))
                .replace(CATEGORY_ALIAS, categorySummary.getAlias());
    }
}
