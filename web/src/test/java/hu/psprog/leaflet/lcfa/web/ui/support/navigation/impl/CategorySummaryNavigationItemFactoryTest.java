package hu.psprog.leaflet.lcfa.web.ui.support.navigation.impl;

import hu.psprog.leaflet.lcfa.core.domain.content.CategorySummary;
import hu.psprog.leaflet.lcfa.web.model.NavigationItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link CategorySummaryNavigationItemFactory}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class CategorySummaryNavigationItemFactoryTest {

    private static final String CATEGORY_TITLE = "Category title";
    private static final CategorySummary CATEGORY_SUMMARY = CategorySummary.builder()
            .id(1L)
            .title(CATEGORY_TITLE)
            .alias("category-alias")
            .build();
    private static final NavigationItem EXPECTED_NAVIGATION_ITEM = NavigationItem.build("/category/1/category-alias", CATEGORY_TITLE);

    @InjectMocks
    private CategorySummaryNavigationItemFactory categorySummaryNavigationItemFactory;

    @Test
    public void shouldCreateNavigationItem() {

        // when
        NavigationItem result = categorySummaryNavigationItemFactory.create(CATEGORY_SUMMARY);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(EXPECTED_NAVIGATION_ITEM));
    }

    @Test
    public void shouldForModelClassReturnClassInstance() {

        // when
        Class<CategorySummary> result = categorySummaryNavigationItemFactory.forModelClass();

        // then
        assertThat(result, notNullValue());
    }
}
