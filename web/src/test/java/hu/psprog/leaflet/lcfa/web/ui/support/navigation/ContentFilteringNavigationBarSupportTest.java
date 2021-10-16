package hu.psprog.leaflet.lcfa.web.ui.support.navigation;

import hu.psprog.leaflet.lcfa.core.domain.content.CategorySummary;
import hu.psprog.leaflet.lcfa.core.domain.content.HomePageContent;
import hu.psprog.leaflet.lcfa.core.domain.content.TagSummary;
import hu.psprog.leaflet.lcfa.web.model.NavigationItem;
import hu.psprog.leaflet.lcfa.web.ui.support.navigation.impl.NavigationItemFactoryRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * Unit tests for {@link ContentFilteringNavigationBarSupport}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class ContentFilteringNavigationBarSupportTest {

    private static final long CATEGORY_ID = 1L;
    private static final long TAG_ID = 2L;
    private static final CategorySummary CATEGORY_SUMMARY = CategorySummary.builder().id(CATEGORY_ID).build();
    private static final TagSummary TAG_SUMMARY = TagSummary.builder().id(TAG_ID).build();
    private static final HomePageContent HOME_PAGE_CONTENT = HomePageContent.builder()
            .categories(Collections.singletonList(CATEGORY_SUMMARY))
            .tags(Collections.singletonList(TAG_SUMMARY))
            .build();
    private static final NavigationItem NAVIGATION_ITEM = NavigationItem.build("link", "title");

    @Mock
    private NavigationItemFactoryRegistry navigationItemFactoryRegistry;

    @Mock
    private NavigationItemFactory<CategorySummary> categorySummaryNavigationItemFactory;

    @Mock
    private NavigationItemFactory<TagSummary> tagSummaryNavigationItemFactory;

    @InjectMocks
    private ContentFilteringNavigationBarSupport contentFilteringNavigationBarSupport;

    @Test
    public void shouldReturnNavigationItemForCategoryFilterPage() {

        // given
        given(navigationItemFactoryRegistry.getFactory(CategorySummary.class)).willReturn(categorySummaryNavigationItemFactory);
        given(categorySummaryNavigationItemFactory.create(CATEGORY_SUMMARY)).willReturn(NAVIGATION_ITEM);

        // when
        NavigationItem result = contentFilteringNavigationBarSupport.categoryFilterPage(HOME_PAGE_CONTENT, CATEGORY_ID);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(NAVIGATION_ITEM));
    }

    @Test
    public void shouldReturnNullForCategoryFilterPageIfCategoryDoesNotExist() {

        // when
        NavigationItem result = contentFilteringNavigationBarSupport.categoryFilterPage(HOME_PAGE_CONTENT, 8L);

        // then
        assertThat(result, nullValue());
        verifyNoInteractions(navigationItemFactoryRegistry, categorySummaryNavigationItemFactory);
    }

    @Test
    public void shouldReturnNavigationItemForTagFilterPage() {

        // given
        given(navigationItemFactoryRegistry.getFactory(TagSummary.class)).willReturn(tagSummaryNavigationItemFactory);
        given(tagSummaryNavigationItemFactory.create(TAG_SUMMARY)).willReturn(NAVIGATION_ITEM);

        // when
        NavigationItem result = contentFilteringNavigationBarSupport.tagFilterPage(HOME_PAGE_CONTENT, TAG_ID);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(NAVIGATION_ITEM));
    }

    @Test
    public void shouldReturnNullForCategoryFilterPageIfTagDoesNotExist() {

        // when
        NavigationItem result = contentFilteringNavigationBarSupport.tagFilterPage(HOME_PAGE_CONTENT, 8L);

        // then
        assertThat(result, nullValue());
        verifyNoInteractions(navigationItemFactoryRegistry, tagSummaryNavigationItemFactory);
    }

    @Test
    public void shouldReturnNavigationItemForContentFilterPage() {

        // when
        NavigationItem result = contentFilteringNavigationBarSupport.contentFilterPage("content-expression");

        // then
        assertThat(result, notNullValue());
        assertThat(result.getLink(), equalTo("/content?content=content-expression"));
        assertThat(result.getTitle(), equalTo("\"content-expression\""));
    }
}
