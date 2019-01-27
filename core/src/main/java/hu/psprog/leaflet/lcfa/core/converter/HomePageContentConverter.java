package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.lcfa.core.domain.content.HomePageContent;
import hu.psprog.leaflet.lcfa.core.domain.raw.HomePageRawResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link HomePageRawResponseWrapper} to {@link HomePageContent}.
 *
 * @author Peter Smith
 */
@Component
public class HomePageContentConverter implements Converter<HomePageRawResponseWrapper, HomePageContent> {

    private WrappedDataExtractor wrappedDataExtractor;
    private CategorySummaryListConverter categorySummaryListConverter;
    private EntrySummaryListConverter entrySummaryListConverter;
    private TagSummaryListConverter tagSummaryListConverter;

    @Autowired
    public HomePageContentConverter(WrappedDataExtractor wrappedDataExtractor, CategorySummaryListConverter categorySummaryListConverter,
                                    EntrySummaryListConverter entrySummaryListConverter, TagSummaryListConverter tagSummaryListConverter) {
        this.wrappedDataExtractor = wrappedDataExtractor;
        this.categorySummaryListConverter = categorySummaryListConverter;
        this.entrySummaryListConverter = entrySummaryListConverter;
        this.tagSummaryListConverter = tagSummaryListConverter;
    }

    @Override
    public HomePageContent convert(HomePageRawResponseWrapper source) {
        return HomePageContent.builder()
                .categories(categorySummaryListConverter.convert(source.getCategoryListDataModel()))
                .tags(tagSummaryListConverter.convert(source.getWrappedTagListDataModel().getBody()))
                .entries(entrySummaryListConverter.convert(source.getWrappedEntryListDataModel().getBody()))
                .pagination(wrappedDataExtractor.extractPaginationAttributes(source.getWrappedEntryListDataModel()))
                .build();
    }
}
