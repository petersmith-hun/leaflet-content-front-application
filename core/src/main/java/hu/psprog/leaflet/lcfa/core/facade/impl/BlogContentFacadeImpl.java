package hu.psprog.leaflet.lcfa.core.facade.impl;

import hu.psprog.leaflet.lcfa.core.config.DefaultPaginationAttributes;
import hu.psprog.leaflet.lcfa.core.domain.content.ArticleContent;
import hu.psprog.leaflet.lcfa.core.domain.content.HomePageContent;
import hu.psprog.leaflet.lcfa.core.domain.content.request.FilteredPaginationContentRequest;
import hu.psprog.leaflet.lcfa.core.domain.content.request.PaginatedContentRequest;
import hu.psprog.leaflet.lcfa.core.domain.raw.ArticlePageRawResponseWrapper;
import hu.psprog.leaflet.lcfa.core.domain.raw.HomePageRawResponseWrapper;
import hu.psprog.leaflet.lcfa.core.exception.ContentNotFoundException;
import hu.psprog.leaflet.lcfa.core.exception.ContentRetrievalException;
import hu.psprog.leaflet.lcfa.core.facade.BlogContentFacade;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link BlogContentFacade}.
 *
 * @author Peter Smith
 */
@Service
public class BlogContentFacadeImpl implements BlogContentFacade {

    private static final String FAILED_TO_RETRIEVE_HOME_PAGE_CONTENT = "Failed to retrieve home page content for page [%d]";
    private static final String FAILED_TO_RETRIEVE_ARTICLE = "Failed to retrieve article for link [%s]";

    private ContentRequestAdapterRegistry contentRequestAdapterRegistry;
    private ConversionService conversionService;
    private DefaultPaginationAttributes defaultPaginationAttributes;

    @Autowired
    public BlogContentFacadeImpl(ContentRequestAdapterRegistry contentRequestAdapterRegistry, ConversionService conversionService,
                                 DefaultPaginationAttributes defaultPaginationAttributes) {
        this.contentRequestAdapterRegistry = contentRequestAdapterRegistry;
        this.conversionService = conversionService;
        this.defaultPaginationAttributes = defaultPaginationAttributes;
    }

    @Override
    public HomePageContent getHomePageContent(int page) {
        return contentRequestAdapterRegistry.<HomePageRawResponseWrapper, PaginatedContentRequest>getContentRequestAdapter(ContentRequestAdapterIdentifier.HOME_PAGE)
                .getContent(createHomePageContentRequest(page))
                .map(homePageRawResponseWrapper -> conversionService.convert(homePageRawResponseWrapper, HomePageContent.class))
                .orElseThrow(() -> new ContentRetrievalException(String.format(FAILED_TO_RETRIEVE_HOME_PAGE_CONTENT, page)));
    }

    @Override
    public ArticleContent getArticle(String link) {
        return contentRequestAdapterRegistry.<ArticlePageRawResponseWrapper, String>getContentRequestAdapter(ContentRequestAdapterIdentifier.ARTICLE)
                .getContent(link)
                .map(articleWrapper -> conversionService.convert(articleWrapper, ArticleContent.class))
                .orElseThrow(() -> new ContentNotFoundException(String.format(FAILED_TO_RETRIEVE_ARTICLE, link)));
    }

    @Override
    public HomePageContent getArticlesByCategory(long categoryID, int page) {
        return contentRequestAdapterRegistry.<HomePageRawResponseWrapper, FilteredPaginationContentRequest<Long>>getContentRequestAdapter(ContentRequestAdapterIdentifier.CATEGORY_FILTER)
                .getContent(createdFilteredPaginatedContentRequest(categoryID, page))
                .map(homePageRawResponseWrapper -> conversionService.convert(homePageRawResponseWrapper, HomePageContent.class))
                .orElseThrow(() -> new ContentRetrievalException(String.format("Failed to retrieve page [%d] of category [%d]", page, categoryID)));
    }

    @Override
    public HomePageContent getArticlesByTag(long tagID, int page) {
        return contentRequestAdapterRegistry.<HomePageRawResponseWrapper, FilteredPaginationContentRequest<Long>>getContentRequestAdapter(ContentRequestAdapterIdentifier.TAG_FILTER)
                .getContent(createdFilteredPaginatedContentRequest(tagID, page))
                .map(homePageRawResponseWrapper -> conversionService.convert(homePageRawResponseWrapper, HomePageContent.class))
                .orElseThrow(() -> new ContentRetrievalException(String.format("Failed to retrieve page [%d] of tag [%d]", page, tagID)));
    }

    @Override
    public HomePageContent getArticlesByContent(String contentExpression, int page) {
        return contentRequestAdapterRegistry.<HomePageRawResponseWrapper, FilteredPaginationContentRequest<String>>getContentRequestAdapter(ContentRequestAdapterIdentifier.CONTENT_FILTER)
                .getContent(createdFilteredPaginatedContentRequest(contentExpression, page))
                .map(homePageRawResponseWrapper -> conversionService.convert(homePageRawResponseWrapper, HomePageContent.class))
                .orElseThrow(() -> new ContentRetrievalException(String.format("Failed to retrieve page [%d] of content expression [%s]", page, contentExpression)));
    }

    private PaginatedContentRequest createHomePageContentRequest(int page) {
        return PaginatedContentRequest.builder()
                .page(page)
                .limit(defaultPaginationAttributes.getLimit())
                .entryOrderBy(defaultPaginationAttributes.getOrderBy())
                .entryOrderDirection(defaultPaginationAttributes.getOrderDirection())
                .build();
    }

    private <T> FilteredPaginationContentRequest<T> createdFilteredPaginatedContentRequest(T filterValue, int page) {
        return FilteredPaginationContentRequest.<T>builder()
                .filterValue(filterValue)
                .page(page)
                .limit(defaultPaginationAttributes.getLimit())
                .entryOrderBy(defaultPaginationAttributes.getOrderBy())
                .entryOrderDirection(defaultPaginationAttributes.getOrderDirection())
                .build();
    }
}
