package hu.psprog.leaflet.lcfa.core.facade.impl;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.document.DocumentDataModel;
import hu.psprog.leaflet.lcfa.core.config.PageConfigModel;
import hu.psprog.leaflet.lcfa.core.domain.content.StaticPageContent;
import hu.psprog.leaflet.lcfa.core.domain.content.StaticPageType;
import hu.psprog.leaflet.lcfa.core.exception.ContentNotFoundException;
import hu.psprog.leaflet.lcfa.core.facade.StaticPageContentFacade;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

import static hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier.STATIC_PAGE;

/**
 * Implementation of {@link StaticPageContentFacade}.
 *
 * @author Peter Smith
 */
@Service
public class StaticPageContentFacadeImpl implements StaticPageContentFacade {

    private static final String FAILED_TO_RETRIEVE_STATIC_PAGE = "Failed to retrieve static page for link [%s]";
    private static final String MAPPING_NOT_FOUND = "Mapping not found for static page type of [%s]";

    private final ContentRequestAdapterRegistry contentRequestAdapterRegistry;
    private final ConversionService conversionService;
    private final Map<StaticPageType, String> staticPageMapping;

    @Autowired
    public StaticPageContentFacadeImpl(ContentRequestAdapterRegistry contentRequestAdapterRegistry,
                                       ConversionService conversionService, PageConfigModel pageConfigModel) {
        this.contentRequestAdapterRegistry = contentRequestAdapterRegistry;
        this.conversionService = conversionService;
        this.staticPageMapping = pageConfigModel.getStaticPageMapping();
    }

    @Override
    public StaticPageContent getStaticPage(StaticPageType staticPageType) {

        String staticPageLink = staticPageMapping.get(staticPageType);
        if (Objects.isNull(staticPageLink)) {
            throw new ContentNotFoundException(String.format(MAPPING_NOT_FOUND, staticPageType));
        }

        return contentRequestAdapterRegistry.<WrapperBodyDataModel<DocumentDataModel>, String>getContentRequestAdapter(STATIC_PAGE)
                .getContent(staticPageLink)
                .map(staticPage -> conversionService.convert(staticPage, StaticPageContent.class))
                .orElseThrow(() -> new ContentNotFoundException(String.format(FAILED_TO_RETRIEVE_STATIC_PAGE, staticPageLink)));
    }
}
