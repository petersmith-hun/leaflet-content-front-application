package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryListDataModel;
import hu.psprog.leaflet.lcfa.core.domain.common.CommonPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link EntryListDataModel} wrapped in {@link WrapperBodyDataModel} to {@link CommonPageData}.
 *
 * @author Peter Smith
 */
@Component
public class CommonPageDataConverter implements Converter<WrapperBodyDataModel<EntryListDataModel>, CommonPageData> {

    private WrappedDataExtractor wrappedDataExtractor;
    private EntrySummaryListConverter entrySummaryListConverter;

    @Autowired
    public CommonPageDataConverter(WrappedDataExtractor wrappedDataExtractor, EntrySummaryListConverter entrySummaryListConverter) {
        this.wrappedDataExtractor = wrappedDataExtractor;
        this.entrySummaryListConverter = entrySummaryListConverter;
    }

    @Override
    public CommonPageData convert(WrapperBodyDataModel<EntryListDataModel> source) {
        return CommonPageData.builder()
                .seo(wrappedDataExtractor.extractSEOAttributes(source))
                .headerMenu(wrappedDataExtractor.extractHeaderMenu(source))
                .footerMenu(wrappedDataExtractor.extractFooterMenu(source))
                .standaloneMenuItems(wrappedDataExtractor.extractStandaloneMenuItems(source))
                .latestEntries(entrySummaryListConverter.convert(source.getBody()))
                .build();
    }
}
