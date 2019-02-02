package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.document.DocumentDataModel;
import hu.psprog.leaflet.lcfa.core.domain.content.StaticPage;
import hu.psprog.leaflet.lcfa.core.domain.content.StaticPageContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link DocumentDataModel} wrapped in {@link WrapperBodyDataModel} to {@link StaticPageContent} object.
 *
 * @author Peter Smith
 */
@Component
public class StaticPageContentConverter implements Converter<WrapperBodyDataModel<DocumentDataModel>, StaticPageContent> {

    private WrappedDataExtractor wrappedDataExtractor;

    @Autowired
    public StaticPageContentConverter(WrappedDataExtractor wrappedDataExtractor) {
        this.wrappedDataExtractor = wrappedDataExtractor;
    }

    @Override
    public StaticPageContent convert(WrapperBodyDataModel<DocumentDataModel> source) {
        return StaticPageContent.builder()
                .page(convert(source.getBody()))
                .seo(wrappedDataExtractor.extractSEOAttributes(source))
                .build();
    }

    private StaticPage convert(DocumentDataModel source) {
        return new StaticPage(source.getTitle(), source.getRawContent());
    }
}
