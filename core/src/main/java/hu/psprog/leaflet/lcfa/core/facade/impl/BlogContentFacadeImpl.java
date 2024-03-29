package hu.psprog.leaflet.lcfa.core.facade.impl;

import hu.psprog.leaflet.lcfa.core.config.DefaultPaginationAttributes;
import hu.psprog.leaflet.lcfa.core.domain.content.ArticleContent;
import hu.psprog.leaflet.lcfa.core.domain.content.HomePageContent;
import hu.psprog.leaflet.lcfa.core.domain.content.request.FilteredPaginationContentRequest;
import hu.psprog.leaflet.lcfa.core.domain.content.request.OrderBy;
import hu.psprog.leaflet.lcfa.core.domain.content.request.PaginatedContentRequest;
import hu.psprog.leaflet.lcfa.core.domain.raw.ArticlePageRawResponseWrapper;
import hu.psprog.leaflet.lcfa.core.domain.raw.HomePageRawResponseWrapper;
import hu.psprog.leaflet.lcfa.core.exception.ContentNotFoundException;
import hu.psprog.leaflet.lcfa.core.exception.ContentRetrievalException;
import hu.psprog.leaflet.lcfa.core.facade.BlogContentFacade;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import static hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier.ARTICLE;
import static hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier.CATEGORY_FILTER;
import static hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier.CONTENT_FILTER;
import static hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier.HOME_PAGE;
import static hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier.TAG_FILTER;

/**
 * Implementation of {@link BlogContentFacade}.
 *
 * @author Peter Smith
 */
@Service
public class BlogContentFacadeImpl implements BlogContentFacade {

    private static final String FAILED_TO_RETRIEVE_HOME_PAGE_CONTENT = "Failed to retrieve home page content for page [%d]";
    private static final String FAILED_TO_RETRIEVE_ARTICLE = "Failed to retrieve article for link [%s]";
    private static final String FAILED_TO_RETRIEVE_PAGE_OF_CATEGORY = "Failed to retrieve page [%d] of category [%d]";
    private static final String FAILED_TO_RETRIEVE_PAGE_OF_TAG = "Failed to retrieve page [%d] of tag [%d]";
    private static final String FAILED_TO_RETRIEVE_PAGE_OF_CONTENT_EXPRESSION = "Failed to retrieve page [%d] of content expression [%s]";

    private final ContentRequestAdapterRegistry contentRequestAdapterRegistry;
    private final ConversionService conversionService;
    private final DefaultPaginationAttributes<OrderBy.Entry> defaultPaginationAttributes;

    @Autowired
    public BlogContentFacadeImpl(ContentRequestAdapterRegistry contentRequestAdapterRegistry, ConversionService conversionService,
                                 DefaultPaginationAttributes<OrderBy.Entry> defaultPaginationAttributes) {
        this.contentRequestAdapterRegistry = contentRequestAdapterRegistry;
        this.conversionService = conversionService;
        this.defaultPaginationAttributes = defaultPaginationAttributes;
    }

    @Override
    public HomePageContent getHomePageContent(int page) {
        return contentRequestAdapterRegistry.<HomePageRawResponseWrapper, PaginatedContentRequest>getContentRequestAdapter(HOME_PAGE)
                .getContent(createHomePageContentRequest(page))
                .map(homePageRawResponseWrapper -> conversionService.convert(homePageRawResponseWrapper, HomePageContent.class))
                .orElseThrow(() -> new ContentRetrievalException(String.format(FAILED_TO_RETRIEVE_HOME_PAGE_CONTENT, page)));
    }

    @Override
    public ArticleContent getArticle(String link) {
        return contentRequestAdapterRegistry.<ArticlePageRawResponseWrapper, String>getContentRequestAdapter(ARTICLE)
                .getContent(link)
                .map(articleWrapper -> conversionService.convert(articleWrapper, ArticleContent.class))
                .orElseThrow(() -> new ContentNotFoundException(String.format(FAILED_TO_RETRIEVE_ARTICLE, link)));
    }

    @Override
    public HomePageContent getArticlesByCategory(long categoryID, int page) {
        return contentRequestAdapterRegistry.<HomePageRawResponseWrapper, FilteredPaginationContentRequest<Long, OrderBy.Entry>>getContentRequestAdapter(CATEGORY_FILTER)
                .getContent(createdFilteredPaginatedContentRequest(categoryID, page))
                .map(homePageRawResponseWrapper -> conversionService.convert(homePageRawResponseWrapper, HomePageContent.class))
                .orElseThrow(() -> new ContentRetrievalException(String.format(FAILED_TO_RETRIEVE_PAGE_OF_CATEGORY, page, categoryID)));
    }

    @Override
    public HomePageContent getArticlesByTag(long tagID, int page) {
        return contentRequestAdapterRegistry.<HomePageRawResponseWrapper, FilteredPaginationContentRequest<Long, OrderBy.Entry>>getContentRequestAdapter(TAG_FILTER)
                .getContent(createdFilteredPaginatedContentRequest(tagID, page))
                .map(homePageRawResponseWrapper -> conversionService.convert(homePageRawResponseWrapper, HomePageContent.class))
                .orElseThrow(() -> new ContentRetrievalException(String.format(FAILED_TO_RETRIEVE_PAGE_OF_TAG, page, tagID)));
    }

    @Override
    public HomePageContent getArticlesByContent(String contentExpression, int page) {
        return contentRequestAdapterRegistry.<HomePageRawResponseWrapper, FilteredPaginationContentRequest<String, OrderBy.Entry>>getContentRequestAdapter(CONTENT_FILTER)
                .getContent(createdFilteredPaginatedContentRequest(contentExpression, page))
                .map(homePageRawResponseWrapper -> conversionService.convert(homePageRawResponseWrapper, HomePageContent.class))
                .orElseThrow(() -> new ContentRetrievalException(String.format(FAILED_TO_RETRIEVE_PAGE_OF_CONTENT_EXPRESSION, page, contentExpression)));
    }

    private PaginatedContentRequest createHomePageContentRequest(int page) {
        return PaginatedContentRequest.builder()
                .page(page)
                .limit(defaultPaginationAttributes.getLimit())
                .entryOrderBy(defaultPaginationAttributes.getOrderBy())
                .entryOrderDirection(defaultPaginationAttributes.getOrderDirection())
                .build();
    }

    private <T> FilteredPaginationContentRequest<T, OrderBy.Entry> createdFilteredPaginatedContentRequest(T filterValue, int page) {
        return FilteredPaginationContentRequest.<T, OrderBy.Entry>builder()
                .filterValue(filterValue)
                .page(page)
                .limit(defaultPaginationAttributes.getLimit())
                .orderBy(defaultPaginationAttributes.getOrderBy())
                .orderDirection(defaultPaginationAttributes.getOrderDirection())
                .build();
    }
}
