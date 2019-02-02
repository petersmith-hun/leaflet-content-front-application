package hu.psprog.leaflet.lcfa.core.facade.impl;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.document.DocumentDataModel;
import hu.psprog.leaflet.lcfa.core.domain.content.StaticPageContent;
import hu.psprog.leaflet.lcfa.core.domain.content.StaticPageType;
import hu.psprog.leaflet.lcfa.core.exception.ContentNotFoundException;
import hu.psprog.leaflet.lcfa.core.facade.StaticPageContentFacade;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of {@link StaticPageContentFacade}.
 *
 * @author Peter Smith
 */
@Service
@ConfigurationProperties(prefix = "page-config")
public class StaticPageContentFacadeImpl implements StaticPageContentFacade {

    private static final String FAILED_TO_RETRIEVE_STATIC_PAGE = "Failed to retrieve static page for link [%s]";

    private ContentRequestAdapter<WrapperBodyDataModel<DocumentDataModel>, String> staticPageContentRequestAdapter;
    private ConversionService conversionService;
    private Map<StaticPageType, String> staticPageMapping = new HashMap<>();

    @Autowired
    public StaticPageContentFacadeImpl(ContentRequestAdapter<WrapperBodyDataModel<DocumentDataModel>, String> staticPageContentRequestAdapter,
                                       ConversionService conversionService) {
        this.staticPageContentRequestAdapter = staticPageContentRequestAdapter;
        this.conversionService = conversionService;
    }

    @Override
    public StaticPageContent getStaticPage(StaticPageType staticPageType) {
        String staticPageLink = staticPageMapping.get(staticPageType);
        return staticPageContentRequestAdapter.getContent(staticPageLink)
                .map(staticPage -> conversionService.convert(staticPage, StaticPageContent.class))
                .orElseThrow(() -> new ContentNotFoundException(String.format(FAILED_TO_RETRIEVE_STATIC_PAGE, staticPageLink)));
    }

    public Map<StaticPageType, String> getStaticPageMapping() {
        return staticPageMapping;
    }
}
