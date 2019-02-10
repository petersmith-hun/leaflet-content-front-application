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

    private ArticleConverter articleConverter;
    private CategorySummaryListConverter categorySummaryListConverter;
    private CommentSummaryListTransformer commentSummaryListTransformer;
    private TagSummaryListConverter tagSummaryListConverter;
    private WrappedDataExtractor wrappedDataExtractor;

    @Autowired
    public ArticleContentConverter(CategorySummaryListConverter categorySummaryListConverter, CommentSummaryListTransformer commentSummaryListTransformer,
                                   TagSummaryListConverter tagSummaryListConverter, WrappedDataExtractor wrappedDataExtractor, ArticleConverter articleConverter) {
        this.categorySummaryListConverter = categorySummaryListConverter;
        this.commentSummaryListTransformer = commentSummaryListTransformer;
        this.tagSummaryListConverter = tagSummaryListConverter;
        this.wrappedDataExtractor = wrappedDataExtractor;
        this.articleConverter = articleConverter;
    }

    @Override
    public ArticleContent convert(ArticlePageRawResponseWrapper source) {
        return ArticleContent.builder()
                .article(articleConverter.convert(source.getWrappedExtendedEntryDataModel()))
                .categories(categorySummaryListConverter.convert(source.getCategoryListDataModel()))
                .tags(tagSummaryListConverter.convert(source.getWrappedTagListDataModel().getBody()))
                .seo(wrappedDataExtractor.extractSEOAttributes(source.getWrappedExtendedEntryDataModel()))
                .comments(commentSummaryListTransformer.convert(source.getWrappedCommentListDataModel().getBody(), source.getWrappedExtendedEntryDataModel().getBody()))
                .build();
    }
}
