package hu.psprog.leaflet.lcfa.core.facade.impl;

import hu.psprog.leaflet.lcfa.core.config.DefaultPaginationAttributes;
import hu.psprog.leaflet.lcfa.core.domain.content.HomePageContent;
import hu.psprog.leaflet.lcfa.core.domain.content.request.PaginatedContentRequest;
import hu.psprog.leaflet.lcfa.core.domain.raw.HomePageRawResponseWrapper;
import hu.psprog.leaflet.lcfa.core.exception.ContentRetrievalException;
import hu.psprog.leaflet.lcfa.core.facade.BlogContentFacade;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapter;
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

    private ContentRequestAdapter<HomePageRawResponseWrapper, PaginatedContentRequest> homePageContentRequestAdapter;
    private ConversionService conversionService;
    private DefaultPaginationAttributes defaultPaginationAttributes;

    @Autowired
    public BlogContentFacadeImpl(ContentRequestAdapter<HomePageRawResponseWrapper, PaginatedContentRequest> homePageContentRequestAdapter,
                                 ConversionService conversionService, DefaultPaginationAttributes defaultPaginationAttributes) {
        this.homePageContentRequestAdapter = homePageContentRequestAdapter;
        this.conversionService = conversionService;
        this.defaultPaginationAttributes = defaultPaginationAttributes;
    }

    @Override
    public HomePageContent getHomePageContent(int page) {
        return homePageContentRequestAdapter.getContent(createHomePageContentRequest(page))
                .map(homePageRawResponseWrapper -> conversionService.convert(homePageRawResponseWrapper, HomePageContent.class))
                .orElseThrow(() -> new ContentRetrievalException(String.format(FAILED_TO_RETRIEVE_HOME_PAGE_CONTENT, page)));
    }

    private PaginatedContentRequest createHomePageContentRequest(int page) {
        return PaginatedContentRequest.builder()
                .page(page)
                .limit(defaultPaginationAttributes.getLimit())
                .entryOrderBy(defaultPaginationAttributes.getOrderBy())
                .entryOrderDirection(defaultPaginationAttributes.getOrderDirection())
                .build();
    }
}
