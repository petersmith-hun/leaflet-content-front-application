package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.lcfa.core.domain.content.HomePageContent;
import hu.psprog.leaflet.lcfa.core.domain.raw.HomePageRawResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Converts {@link HomePageRawResponseWrapper} to {@link HomePageContent}.
 *
 * @author Peter Smith
 */
@Component
public class HomePageContentConverter implements Converter<HomePageRawResponseWrapper, HomePageContent> {

    private WrappedDataExtractor wrappedDataExtractor;
    private EntrySummaryListConverter entrySummaryListConverter;
    private TagSummaryListConverter tagSummaryListConverter;
    private FilteringDataConversionSupport filteringDataConversionSupport;

    @Autowired
    public HomePageContentConverter(WrappedDataExtractor wrappedDataExtractor, EntrySummaryListConverter entrySummaryListConverter,
                                    TagSummaryListConverter tagSummaryListConverter, FilteringDataConversionSupport filteringDataConversionSupport) {
        this.wrappedDataExtractor = wrappedDataExtractor;
        this.entrySummaryListConverter = entrySummaryListConverter;
        this.tagSummaryListConverter = tagSummaryListConverter;
        this.filteringDataConversionSupport = filteringDataConversionSupport;
    }

    @Override
    public HomePageContent convert(HomePageRawResponseWrapper source) {

        HomePageContent homePageContent = null;
        if (Objects.nonNull(source.getWrappedEntryListDataModel())) {
            homePageContent = HomePageContent.builder()
                    .categories(filteringDataConversionSupport.mapCategories(source.getCategoryListDataModel()))
                    .tags(filteringDataConversionSupport.mapOptionalWrapped(source.getWrappedTagListDataModel(), tagSummaryListConverter))
                    .entries(entrySummaryListConverter.convert(source.getWrappedEntryListDataModel().getBody()))
                    .pagination(wrappedDataExtractor.extractPaginationAttributes(source.getWrappedEntryListDataModel()))
                    .build();
        }

        return homePageContent;
    }
}
