package hu.psprog.leaflet.lcfa.core.facade.impl;

import hu.psprog.leaflet.lcfa.core.config.DefaultPaginationAttributes;
import hu.psprog.leaflet.lcfa.core.domain.content.ArticleContent;
import hu.psprog.leaflet.lcfa.core.domain.content.HomePageContent;
import hu.psprog.leaflet.lcfa.core.domain.content.request.FilteredPaginationContentRequest;
import hu.psprog.leaflet.lcfa.core.domain.content.request.OrderBy;
import hu.psprog.leaflet.lcfa.core.domain.content.request.OrderDirection;
import hu.psprog.leaflet.lcfa.core.domain.content.request.PaginatedContentRequest;
import hu.psprog.leaflet.lcfa.core.domain.raw.ArticlePageRawResponseWrapper;
import hu.psprog.leaflet.lcfa.core.domain.raw.HomePageRawResponseWrapper;
import hu.psprog.leaflet.lcfa.core.exception.ContentNotFoundException;
import hu.psprog.leaflet.lcfa.core.exception.ContentRetrievalException;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapter;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterRegistry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link BlogContentFacadeImpl}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class BlogContentFacadeImplTest {

    private static final HomePageRawResponseWrapper HOME_PAGE_RAW_RESPONSE_WRAPPER = HomePageRawResponseWrapper.builder().build();
    private static final ArticlePageRawResponseWrapper ARTICLE_PAGE_RAW_RESPONSE_WRAPPER = ArticlePageRawResponseWrapper.builder().build();
    private static final HomePageContent HOME_PAGE_CONTENT = HomePageContent.builder().build();
    private static final ArticleContent ARTICLE_CONTENT = ArticleContent.builder().build();
    private static final int ITEM_LIMIT_ON_PAGE = 10;
    private static final OrderBy.Entry ORDER_BY = OrderBy.Entry.CREATED;
    private static final OrderDirection ORDER_DIRECTION = OrderDirection.DESC;
    private static final int PAGE_NUMBER = 1;
    private static final String ARTICLE_LINK = "article-link";
    private static final PaginatedContentRequest HOME_PAGE_PAGINATED_CONTENT_REQUEST = preparePaginatedContentRequest();
    private static final long CATEGORY_ID = 3L;
    private static final long TAG_ID = 4L;
    private static final String SEARCH_EXPRESSION = "search expression";
    private static final FilteredPaginationContentRequest<Long, OrderBy.Entry> CATEGORY_FILTERED_CONTENT_REQUEST = preparePaginatedContentRequest(CATEGORY_ID);
    private static final FilteredPaginationContentRequest<Long, OrderBy.Entry> TAG_FILTERED_CONTENT_REQUEST = preparePaginatedContentRequest(TAG_ID);
    private static final FilteredPaginationContentRequest<String, OrderBy.Entry> EXPRESSION_FILTERED_CONTENT_REQUEST = preparePaginatedContentRequest(SEARCH_EXPRESSION);

    @Mock
    private ContentRequestAdapterRegistry contentRequestAdapterRegistry;

    @Mock
    private ConversionService conversionService;

    @Mock
    private DefaultPaginationAttributes<OrderBy.Entry> defaultPaginationAttributes;

    @Mock
    private ContentRequestAdapter<HomePageRawResponseWrapper, PaginatedContentRequest> homePageContentRequestAdapter;

    @Mock
    private ContentRequestAdapter<HomePageRawResponseWrapper, FilteredPaginationContentRequest<Long, OrderBy.Entry>> longValueFilteredPageContentRequestAdapter;

    @Mock
    private ContentRequestAdapter<HomePageRawResponseWrapper, FilteredPaginationContentRequest<String, OrderBy.Entry>> stringValueFilteredPageContentRequestAdapter;

    @Mock
    private ContentRequestAdapter<ArticlePageRawResponseWrapper, String> articleContentRequestAdapter;

    @InjectMocks
    private BlogContentFacadeImpl blogContentFacade;

    @Test
    public void shouldGetHomePageContentWithSuccess() {

        // given
        givenPagination();
        given(contentRequestAdapterRegistry.<HomePageRawResponseWrapper, PaginatedContentRequest>getContentRequestAdapter(ContentRequestAdapterIdentifier.HOME_PAGE))
                .willReturn(homePageContentRequestAdapter);
        given(homePageContentRequestAdapter.getContent(HOME_PAGE_PAGINATED_CONTENT_REQUEST)).willReturn(Optional.of(HOME_PAGE_RAW_RESPONSE_WRAPPER));
        given(conversionService.convert(HOME_PAGE_RAW_RESPONSE_WRAPPER, HomePageContent.class)).willReturn(HOME_PAGE_CONTENT);

        // when
        HomePageContent result = blogContentFacade.getHomePageContent(PAGE_NUMBER);

        // then
        assertThat(result, equalTo(HOME_PAGE_CONTENT));
    }

    @Test
    public void shouldGetHomePageContentThrowContentRetrievalExceptionForMissingData() {

        // given
        givenPagination();
        given(contentRequestAdapterRegistry.<HomePageRawResponseWrapper, PaginatedContentRequest>getContentRequestAdapter(ContentRequestAdapterIdentifier.HOME_PAGE))
                .willReturn(homePageContentRequestAdapter);
        given(homePageContentRequestAdapter.getContent(HOME_PAGE_PAGINATED_CONTENT_REQUEST)).willReturn(Optional.empty());

        // when
        Assertions.assertThrows(ContentRetrievalException.class, () -> blogContentFacade.getHomePageContent(PAGE_NUMBER));

        // then
        // exception expected
    }

    @Test
    public void shouldGetArticleWithSuccess() {

        // given
        given(contentRequestAdapterRegistry.<ArticlePageRawResponseWrapper, String>getContentRequestAdapter(ContentRequestAdapterIdentifier.ARTICLE))
                .willReturn(articleContentRequestAdapter);
        given(articleContentRequestAdapter.getContent(ARTICLE_LINK)).willReturn(Optional.of(ARTICLE_PAGE_RAW_RESPONSE_WRAPPER));
        given(conversionService.convert(ARTICLE_PAGE_RAW_RESPONSE_WRAPPER, ArticleContent.class)).willReturn(ARTICLE_CONTENT);

        // when
        ArticleContent result = blogContentFacade.getArticle(ARTICLE_LINK);

        // then
        assertThat(result, equalTo(ARTICLE_CONTENT));
    }

    @Test
    public void shouldGetArticleThrowContentNotFondExceptionForMissingData() {

        // given
        given(contentRequestAdapterRegistry.<ArticlePageRawResponseWrapper, String>getContentRequestAdapter(ContentRequestAdapterIdentifier.ARTICLE))
                .willReturn(articleContentRequestAdapter);
        given(articleContentRequestAdapter.getContent(ARTICLE_LINK)).willReturn(Optional.empty());

        // when
        Assertions.assertThrows(ContentNotFoundException.class, () -> blogContentFacade.getArticle(ARTICLE_LINK));

        // then
        // exception expected
    }

    @Test
    public void shouldGetArticlesByCategoryWithSuccess() {

        // given
        givenPagination();
        given(contentRequestAdapterRegistry.<HomePageRawResponseWrapper, FilteredPaginationContentRequest<Long, OrderBy.Entry>>getContentRequestAdapter(ContentRequestAdapterIdentifier.CATEGORY_FILTER))
                .willReturn(longValueFilteredPageContentRequestAdapter);
        given(longValueFilteredPageContentRequestAdapter.getContent(CATEGORY_FILTERED_CONTENT_REQUEST)).willReturn(Optional.of(HOME_PAGE_RAW_RESPONSE_WRAPPER));
        given(conversionService.convert(HOME_PAGE_RAW_RESPONSE_WRAPPER, HomePageContent.class)).willReturn(HOME_PAGE_CONTENT);

        // when
        HomePageContent result = blogContentFacade.getArticlesByCategory(CATEGORY_ID, PAGE_NUMBER);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(HOME_PAGE_CONTENT));
    }

    @Test
    public void shouldGetArticlesByCategoryThrowContentRetrievalExceptionForMissingData() {

        // given
        givenPagination();
        given(contentRequestAdapterRegistry.<HomePageRawResponseWrapper, FilteredPaginationContentRequest<Long, OrderBy.Entry>>getContentRequestAdapter(ContentRequestAdapterIdentifier.CATEGORY_FILTER))
                .willReturn(longValueFilteredPageContentRequestAdapter);
        given(longValueFilteredPageContentRequestAdapter.getContent(CATEGORY_FILTERED_CONTENT_REQUEST)).willReturn(Optional.empty());

        // when
        Assertions.assertThrows(ContentRetrievalException.class, () -> blogContentFacade.getArticlesByCategory(CATEGORY_ID, PAGE_NUMBER));

        // then
        // exception expected
    }

    @Test
    public void shouldGetArticlesByTagWithSuccess() {

        // given
        givenPagination();
        given(contentRequestAdapterRegistry.<HomePageRawResponseWrapper, FilteredPaginationContentRequest<Long, OrderBy.Entry>>getContentRequestAdapter(ContentRequestAdapterIdentifier.TAG_FILTER))
                .willReturn(longValueFilteredPageContentRequestAdapter);
        given(longValueFilteredPageContentRequestAdapter.getContent(TAG_FILTERED_CONTENT_REQUEST)).willReturn(Optional.of(HOME_PAGE_RAW_RESPONSE_WRAPPER));
        given(conversionService.convert(HOME_PAGE_RAW_RESPONSE_WRAPPER, HomePageContent.class)).willReturn(HOME_PAGE_CONTENT);

        // when
        HomePageContent result = blogContentFacade.getArticlesByTag(TAG_ID, PAGE_NUMBER);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(HOME_PAGE_CONTENT));
    }

    @Test
    public void shouldGetArticlesByTagThrowContentRetrievalExceptionForMissingData() {

        // given
        givenPagination();
        given(contentRequestAdapterRegistry.<HomePageRawResponseWrapper, FilteredPaginationContentRequest<Long, OrderBy.Entry>>getContentRequestAdapter(ContentRequestAdapterIdentifier.TAG_FILTER))
                .willReturn(longValueFilteredPageContentRequestAdapter);
        given(longValueFilteredPageContentRequestAdapter.getContent(TAG_FILTERED_CONTENT_REQUEST)).willReturn(Optional.empty());

        // when
        Assertions.assertThrows(ContentRetrievalException.class, () -> blogContentFacade.getArticlesByTag(TAG_ID, PAGE_NUMBER));

        // then
        // exception expected
    }

    @Test
    public void shouldGetArticlesByContentExpressionWithSuccess() {

        // given
        givenPagination();
        given(contentRequestAdapterRegistry.<HomePageRawResponseWrapper, FilteredPaginationContentRequest<String, OrderBy.Entry>>getContentRequestAdapter(ContentRequestAdapterIdentifier.CONTENT_FILTER))
                .willReturn(stringValueFilteredPageContentRequestAdapter);
        given(stringValueFilteredPageContentRequestAdapter.getContent(EXPRESSION_FILTERED_CONTENT_REQUEST)).willReturn(Optional.of(HOME_PAGE_RAW_RESPONSE_WRAPPER));
        given(conversionService.convert(HOME_PAGE_RAW_RESPONSE_WRAPPER, HomePageContent.class)).willReturn(HOME_PAGE_CONTENT);

        // when
        HomePageContent result = blogContentFacade.getArticlesByContent(SEARCH_EXPRESSION, PAGE_NUMBER);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(HOME_PAGE_CONTENT));
    }

    @Test
    public void shouldGetArticlesByContentExpressionThrowContentRetrievalExceptionForMissingData() {

        // given
        givenPagination();
        given(contentRequestAdapterRegistry.<HomePageRawResponseWrapper, FilteredPaginationContentRequest<String, OrderBy.Entry>>getContentRequestAdapter(ContentRequestAdapterIdentifier.CONTENT_FILTER))
                .willReturn(stringValueFilteredPageContentRequestAdapter);
        given(stringValueFilteredPageContentRequestAdapter.getContent(EXPRESSION_FILTERED_CONTENT_REQUEST)).willReturn(Optional.empty());

        // when
        Assertions.assertThrows(ContentRetrievalException.class, () -> blogContentFacade.getArticlesByContent(SEARCH_EXPRESSION, PAGE_NUMBER));

        // then
        // exception expected
    }

    private static PaginatedContentRequest preparePaginatedContentRequest() {
        return PaginatedContentRequest.builder()
                .page(PAGE_NUMBER)
                .limit(ITEM_LIMIT_ON_PAGE)
                .entryOrderBy(ORDER_BY)
                .entryOrderDirection(ORDER_DIRECTION)
                .build();
    }

    private static <T> FilteredPaginationContentRequest<T, OrderBy.Entry> preparePaginatedContentRequest(T filterValue) {
        return FilteredPaginationContentRequest.<T, OrderBy.Entry>builder()
                .filterValue(filterValue)
                .page(PAGE_NUMBER)
                .limit(ITEM_LIMIT_ON_PAGE)
                .orderBy(ORDER_BY)
                .orderDirection(ORDER_DIRECTION)
                .build();
    }

    private void givenPagination() {
        given(defaultPaginationAttributes.getLimit()).willReturn(ITEM_LIMIT_ON_PAGE);
        given(defaultPaginationAttributes.getOrderBy()).willReturn(ORDER_BY);
        given(defaultPaginationAttributes.getOrderDirection()).willReturn(ORDER_DIRECTION);
    }
}
