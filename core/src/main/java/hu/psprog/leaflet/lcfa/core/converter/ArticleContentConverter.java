package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryDataModel;
import hu.psprog.leaflet.api.rest.response.entry.ExtendedEntryDataModel;
import hu.psprog.leaflet.lcfa.core.domain.content.Article;
import hu.psprog.leaflet.lcfa.core.domain.content.ArticleContent;
import hu.psprog.leaflet.lcfa.core.domain.content.AuthorSummary;
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

    private CategorySummaryListConverter categorySummaryListConverter;
    private TagSummaryListConverter tagSummaryListConverter;

    @Autowired
    public ArticleContentConverter(CategorySummaryListConverter categorySummaryListConverter, TagSummaryListConverter tagSummaryListConverter) {
        this.categorySummaryListConverter = categorySummaryListConverter;
        this.tagSummaryListConverter = tagSummaryListConverter;
    }

    @Override
    public ArticleContent convert(ArticlePageRawResponseWrapper source) {
        return ArticleContent.builder()
                .article(convert(source.getWrappedExtendedEntryDataModel()))
                .categories(categorySummaryListConverter.convert(source.getCategoryListDataModel()))
                .tags(tagSummaryListConverter.convert(source.getWrappedTagListDataModel().getBody()))
                .build();
    }

    private Article convert(WrapperBodyDataModel<ExtendedEntryDataModel> source) {
        return Article.builder()
                .author(createAuthorSummary(source.getBody()))
                .content(source.getBody().getRawContent())
                .creationDate(source.getBody().getCreated())
                .link(source.getBody().getLink())
                .title(source.getBody().getTitle())
                .tags(tagSummaryListConverter.convert(source.getBody().getTags()))
                .build();
    }

    private AuthorSummary createAuthorSummary(EntryDataModel entryDataModel) {
        return new AuthorSummary(entryDataModel.getUser().getUsername());
    }
}
