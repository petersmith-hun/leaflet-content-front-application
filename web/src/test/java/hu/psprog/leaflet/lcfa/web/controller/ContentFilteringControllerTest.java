package hu.psprog.leaflet.lcfa.web.controller;

import hu.psprog.leaflet.lcfa.core.domain.common.PaginationAttributes;
import hu.psprog.leaflet.lcfa.core.domain.content.CategorySummary;
import hu.psprog.leaflet.lcfa.core.domain.content.EntrySummary;
import hu.psprog.leaflet.lcfa.core.domain.content.HomePageContent;
import hu.psprog.leaflet.lcfa.core.domain.content.TagSummary;
import hu.psprog.leaflet.lcfa.core.facade.BlogContentFacade;
import hu.psprog.leaflet.lcfa.web.model.ModelField;
import hu.psprog.leaflet.lcfa.web.model.NavigationItem;
import hu.psprog.leaflet.lcfa.web.ui.support.navigation.ContentFilteringNavigationBarSupport;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link ContentFilteringController}.
 *
 * @author Peter Smith
 */
@RunWith(JUnitParamsRunner.class)
public class ContentFilteringControllerTest extends AbstractControllerTest {

    private static final EntrySummary ENTRY_SUMMARY = EntrySummary.builder().prologue("prologue").build();
    private static final CategorySummary CATEGORY_SUMMARY = CategorySummary.builder().title("category").build();
    private static final TagSummary TAG_SUMMARY = TagSummary.builder().name("tag").build();
    private static final PaginationAttributes PAGINATION_ATTRIBUTES = PaginationAttributes.builder().pageNumber(1).build();
    private static final List<EntrySummary> ENTRY_SUMMARY_LIST = Collections.singletonList(ENTRY_SUMMARY);
    private static final List<CategorySummary> CATEGORY_SUMMARY_LIST = Collections.singletonList(CATEGORY_SUMMARY);
    private static final List<TagSummary> TAG_SUMMARY_LIST = Collections.singletonList(TAG_SUMMARY);
    private static final HomePageContent HOME_PAGE_CONTENT = HomePageContent.builder()
            .entries(ENTRY_SUMMARY_LIST)
            .categories(CATEGORY_SUMMARY_LIST)
            .tags(TAG_SUMMARY_LIST)
            .pagination(PAGINATION_ATTRIBUTES)
            .build();
    private static final long CATEGORY_ID = 3L;
    private static final long TAG_ID = 4L;
    private static final String CONTENT_EXPRESSION = "content-expression";
    private static final String CATEGORY_ALIAS = "category-alias";
    private static final String TAG_ALIAS = "tag-alias";
    private static final NavigationItem CATEGORY_NAVIGATION = NavigationItem.build("category-link-1", "Category navigation");
    private static final NavigationItem TAG_NAVIGATION = NavigationItem.build("tag-link-1", "Tag navigation");
    private static final NavigationItem CONTENT_NAVIGATION = NavigationItem.build("content-link-1", "Content navigation");

    private static final String PAGINATION_LINK_CATEGORY_TEMPLATE = "/category/3/category-alias/page/{page}";
    private static final String PAGINATION_LINK_TAG_TEMPLATE = "/tag/4/tag-alias/page/{page}";
    private static final String PAGINATION_LINK_CONTENT_TEMPLATE = "/content/page/{page}?content=content-expression";
    private static final String PAGINATION_LINK_HOME_TEMPLATE = "/page/{page}";

    private static final String VIEW_GROUP_BLOG = "blog";
    private static final String VIEW_LIST = "list";

    @Mock
    private BlogContentFacade blogContentFacade;

    @Mock
    private ContentFilteringNavigationBarSupport navigationBarSupport;

    @InjectMocks
    private ContentFilteringController contentFilteringController;

    @Test
    @Parameters(source = PageParameterProvider.class)
    public void shouldRenderHomePage(Integer receivedPageNumber, int expectedPageNumber) {

        // given
        given(blogContentFacade.getHomePageContent(expectedPageNumber)).willReturn(HOME_PAGE_CONTENT);

        // when
        contentFilteringController.renderHomePage(Optional.ofNullable(receivedPageNumber));

        // then
        verifyEntryList(expectedPageNumber, PAGINATION_LINK_HOME_TEMPLATE, null);
    }

    @Test
    @Parameters(source = PageParameterProvider.class)
    public void shouldArticleListByCategory(Integer receivedPageNumber, int expectedPageNumber) {

        // given
        given(blogContentFacade.getArticlesByCategory(CATEGORY_ID, expectedPageNumber)).willReturn(HOME_PAGE_CONTENT);
        given(navigationBarSupport.categoryFilterPage(HOME_PAGE_CONTENT, CATEGORY_ID)).willReturn(CATEGORY_NAVIGATION);

        // when
        contentFilteringController.renderArticleListByCategory(CATEGORY_ID, CATEGORY_ALIAS, Optional.ofNullable(receivedPageNumber));

        // then
        verifyEntryList(expectedPageNumber, PAGINATION_LINK_CATEGORY_TEMPLATE, Collections.singletonList(CATEGORY_NAVIGATION));
    }

    @Test
    @Parameters(source = PageParameterProvider.class)
    public void shouldArticleListByTag(Integer receivedPageNumber, int expectedPageNumber) {

        // given
        given(blogContentFacade.getArticlesByTag(TAG_ID, expectedPageNumber)).willReturn(HOME_PAGE_CONTENT);
        given(navigationBarSupport.tagFilterPage(HOME_PAGE_CONTENT, TAG_ID)).willReturn(TAG_NAVIGATION);

        // when
        contentFilteringController.renderArticleListByTag(TAG_ID, TAG_ALIAS, Optional.ofNullable(receivedPageNumber));

        // then
        verifyEntryList(expectedPageNumber, PAGINATION_LINK_TAG_TEMPLATE, Collections.singletonList(TAG_NAVIGATION));
    }

    @Test
    @Parameters(source = PageParameterProvider.class)
    public void shouldArticleListByContent(Integer receivedPageNumber, int expectedPageNumber) {

        // given
        given(blogContentFacade.getArticlesByContent(CONTENT_EXPRESSION, expectedPageNumber)).willReturn(HOME_PAGE_CONTENT);
        given(navigationBarSupport.contentFilterPage(CONTENT_EXPRESSION)).willReturn(CONTENT_NAVIGATION);

        // when
        contentFilteringController.renderArticleListByContent(CONTENT_EXPRESSION, Optional.ofNullable(receivedPageNumber));

        // then
        verifyEntryList(expectedPageNumber, PAGINATION_LINK_CONTENT_TEMPLATE, Collections.singletonList(CONTENT_NAVIGATION));
    }

    public static class PageParameterProvider {

        public static Object[] provide() {
            return new Object[] {
                    new Object[] {null, 1},
                    new Object[] {1, 1},
                    new Object[] {5, 5}};
        }
    }

    @Override
    String controllerViewGroup() {
        return VIEW_GROUP_BLOG;
    }

    private void verifyEntryList(int expectedPageNumber, String linkTemplate, List<NavigationItem> navigationItemList) {
        verifyViewCreated(VIEW_LIST);
        verifyFieldSet(ModelField.LIST_ENTRIES, ENTRY_SUMMARY_LIST);
        verifyFieldSet(ModelField.LIST_CATEGORIES, CATEGORY_SUMMARY_LIST);
        verifyFieldSet(ModelField.LIST_TAGS, TAG_SUMMARY_LIST);
        verifyFieldSet(ModelField.PAGINATION, PAGINATION_ATTRIBUTES);
        verifyFieldSet(ModelField.CURRENT_PAGE_NUMBER, expectedPageNumber);
        verifyFieldSet(ModelField.LINK_TEMPLATE, linkTemplate);
        verifyFieldSet(ModelField.NAVIGATION, navigationItemList);
    }
}
