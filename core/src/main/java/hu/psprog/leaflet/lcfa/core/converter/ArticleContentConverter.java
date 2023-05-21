package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.ExtendedEntryDataModel;
import hu.psprog.leaflet.lcfa.core.domain.content.Article;
import hu.psprog.leaflet.lcfa.core.domain.content.ArticleContent;
import hu.psprog.leaflet.lcfa.core.domain.raw.ArticlePageRawResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link ExtendedEntryDataModel} wrapped in {@link WrapperBodyDataModel} to {@link Article} object.
 *
 * @author Peter Smith
 */
@Component
public class ArticleContentConverter implements Converter<ArticlePageRawResponseWrapper, ArticleContent> {

    private final ArticleConverter articleConverter;
    private final TagSummaryListConverter tagSummaryListConverter;
    private final WrappedDataExtractor wrappedDataExtractor;
    private final FilteringDataConversionSupport filteringDataConversionSupport;

    @Autowired
    public ArticleContentConverter(TagSummaryListConverter tagSummaryListConverter, WrappedDataExtractor wrappedDataExtractor,
                                   ArticleConverter articleConverter, FilteringDataConversionSupport filteringDataConversionSupport) {
        this.tagSummaryListConverter = tagSummaryListConverter;
        this.wrappedDataExtractor = wrappedDataExtractor;
        this.articleConverter = articleConverter;
        this.filteringDataConversionSupport = filteringDataConversionSupport;
    }

    @Override
    public ArticleContent convert(ArticlePageRawResponseWrapper source) {
        return ArticleContent.builder()
                .article(articleConverter.convert(source.wrappedExtendedEntryDataModel()))
                .categories(filteringDataConversionSupport.mapCategories(source.categoryListDataModel()))
                .tags(filteringDataConversionSupport.mapOptionalWrapped(source.wrappedTagListDataModel(), tagSummaryListConverter))
                .seo(wrappedDataExtractor.extractSEOAttributes(source.wrappedExtendedEntryDataModel()))
                .comments(filteringDataConversionSupport.mapComments(source.wrappedCommentListDataModel(), source.wrappedExtendedEntryDataModel().body()))
                .build();
    }
}
