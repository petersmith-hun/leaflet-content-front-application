package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.api.rest.response.comment.CommentDataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryDataModel;
import hu.psprog.leaflet.api.rest.response.entry.ExtendedEntryDataModel;
import hu.psprog.leaflet.api.rest.response.user.UserDataModel;
import hu.psprog.leaflet.lcfa.core.domain.content.Article;
import hu.psprog.leaflet.lcfa.core.domain.content.ArticleContent;
import hu.psprog.leaflet.lcfa.core.domain.content.AuthorSummary;
import hu.psprog.leaflet.lcfa.core.domain.raw.ArticlePageRawResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Converts {@link ExtendedEntryDataModel} wrapped in {@link WrapperBodyDataModel} to {@link Article} object.
 *
 * @author Peter Smith
 */
@Component
public class ArticleContentConverter implements Converter<ArticlePageRawResponseWrapper, ArticleContent> {

    private AttachmentSummaryListConverter attachmentSummaryListConverter;
    private CategorySummaryListConverter categorySummaryListConverter;
    private CommentSummaryListTransformer commentSummaryListTransformer;
    private TagSummaryListConverter tagSummaryListConverter;
    private WrappedDataExtractor wrappedDataExtractor;

    @Autowired
    public ArticleContentConverter(AttachmentSummaryListConverter attachmentSummaryListConverter, CategorySummaryListConverter categorySummaryListConverter,
                                   CommentSummaryListTransformer commentSummaryListTransformer, TagSummaryListConverter tagSummaryListConverter,
                                   WrappedDataExtractor wrappedDataExtractor) {
        this.attachmentSummaryListConverter = attachmentSummaryListConverter;
        this.categorySummaryListConverter = categorySummaryListConverter;
        this.commentSummaryListTransformer = commentSummaryListTransformer;
        this.tagSummaryListConverter = tagSummaryListConverter;
        this.wrappedDataExtractor = wrappedDataExtractor;
    }

    @Override
    public ArticleContent convert(ArticlePageRawResponseWrapper source) {
        return ArticleContent.builder()
                .article(convert(source.getWrappedExtendedEntryDataModel()))
                .categories(categorySummaryListConverter.convert(source.getCategoryListDataModel()))
                .tags(tagSummaryListConverter.convert(source.getWrappedTagListDataModel().getBody()))
                .seo(wrappedDataExtractor.extractSEOAttributes(source.getWrappedExtendedEntryDataModel()))
                .comments(commentSummaryListTransformer.convert(createDummyComments(), source.getWrappedExtendedEntryDataModel().getBody())) // TODO fix after LFLT-325 is done
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
                .attachments(attachmentSummaryListConverter.convert(source.getBody().getAttachments()))
                .build();
    }

    private AuthorSummary createAuthorSummary(EntryDataModel entryDataModel) {
        return new AuthorSummary(entryDataModel.getUser().getUsername());
    }

    private List<CommentDataModel> createDummyComments() {
        return Arrays.asList(
                CommentDataModel.getBuilder()
                        .withContent("comment 1")
                        .withCreated("formatted creation date 1")
                        .withOwner(UserDataModel.getBuilder()
                                .withUsername("user 1")
                                .build())
                        .build(),
                CommentDataModel.getBuilder()
                        .withContent("comment 2")
                        .withCreated("formatted creation date 2")
                        .withOwner(UserDataModel.getBuilder()
                                .withId(1L)
                                .withUsername("user 2")
                                .build())
                        .build());
    }
}
