package hu.psprog.leaflet.lcfa.web.ui.support.navigation;

import hu.psprog.leaflet.lcfa.core.domain.content.CategorySummary;
import hu.psprog.leaflet.lcfa.core.domain.content.HomePageContent;
import hu.psprog.leaflet.lcfa.core.domain.content.TagSummary;
import hu.psprog.leaflet.lcfa.web.controller.BaseController;
import hu.psprog.leaflet.lcfa.web.model.NavigationItem;
import hu.psprog.leaflet.lcfa.web.ui.support.navigation.impl.NavigationItemFactoryRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Support logic for generating navigation bars on content filtering pages.
 *
 * @author Peter Smith
 */
@Component
public class ContentFilteringNavigationBarSupport {

    private static final String CONTENT_FILTER_PATH = BaseController.PATH_FILTER_BY_CONTENT + "?content=%s";
    private static final String CONTENT_EXPRESSION_AS_TITLE = "\"%s\"";

    private final NavigationItemFactoryRegistry navigationItemFactoryRegistry;

    @Autowired
    public ContentFilteringNavigationBarSupport(NavigationItemFactoryRegistry navigationItemFactoryRegistry) {
        this.navigationItemFactoryRegistry = navigationItemFactoryRegistry;
    }

    /**
     * Generates {@link NavigationItem} for category filtering page.
     *
     * @param homePageContent {@link HomePageContent} object containing list of existing {@link CategorySummary} objects
     * @param categoryID ID of the category to be converted to {@link NavigationItem}
     * @return generated {@link NavigationItem} object or {@code null} if requested category does not exist
     */
    public NavigationItem categoryFilterPage(HomePageContent homePageContent, Long categoryID) {
        return extractCategorySummary(homePageContent, categoryID)
                .map(categorySummary -> navigationItemFactoryRegistry
                        .getFactory(CategorySummary.class)
                        .create(categorySummary))
                .orElse(null);
    }

    /**
     * Generates {@link NavigationItem} for tag filtering page.
     *
     * @param homePageContent {@link HomePageContent} object containing list of existing {@link TagSummary} objects
     * @param tagID ID of the tag to be converted to {@link NavigationItem}
     * @return generated {@link NavigationItem} object or {@code null} if requested tag does not exist
     */
    public NavigationItem tagFilterPage(HomePageContent homePageContent, Long tagID) {
        return extractTagSummary(homePageContent, tagID)
                .map(tagSummary -> navigationItemFactoryRegistry
                        .getFactory(TagSummary.class)
                        .create(tagSummary))
                .orElse(null);
    }

    /**
     * Generates {@link NavigationItem} for content filtering page.
     *
     * @param contentExpression content expression to be added to the URL
     * @return generated {@link NavigationItem} object
     */
    public NavigationItem contentFilterPage(String contentExpression) {
        return NavigationItem.build(String.format(CONTENT_FILTER_PATH, contentExpression), transformContactExpressionToTitle(contentExpression));
    }

    private Optional<CategorySummary> extractCategorySummary(HomePageContent homePageContent, Long categoryID) {
        return homePageContent.categories().stream()
                .filter(category -> category.id().equals(categoryID))
                .findFirst();
    }

    private Optional<TagSummary> extractTagSummary(HomePageContent homePageContent, Long tagID) {
        return homePageContent.tags().stream()
                .filter(tagSummary -> tagSummary.id().equals(tagID))
                .findFirst();
    }

    private String transformContactExpressionToTitle(String contentExpression) {
        return String.format(CONTENT_EXPRESSION_AS_TITLE, contentExpression);
    }
}
