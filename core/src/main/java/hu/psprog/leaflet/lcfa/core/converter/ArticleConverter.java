package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryDataModel;
import hu.psprog.leaflet.api.rest.response.entry.ExtendedEntryDataModel;
import hu.psprog.leaflet.lcfa.core.domain.content.Article;
import hu.psprog.leaflet.lcfa.core.domain.content.AuthorSummary;
import hu.psprog.leaflet.lcfa.core.formatter.DateFormatterUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link ExtendedEntryDataModel} wrapped as {@link WrapperBodyDataModel} to {@link Article}.
 *
 * @author Peter Smith
 */
@Component
public class ArticleConverter implements Converter<WrapperBodyDataModel<ExtendedEntryDataModel>, Article> {

    private AttachmentSummaryListConverter attachmentSummaryListConverter;
    private TagSummaryListConverter tagSummaryListConverter;
    private DateFormatterUtility dateFormatterUtility;

    @Autowired
    public ArticleConverter(AttachmentSummaryListConverter attachmentSummaryListConverter, TagSummaryListConverter tagSummaryListConverter,
                            DateFormatterUtility dateFormatterUtility) {
        this.attachmentSummaryListConverter = attachmentSummaryListConverter;
        this.tagSummaryListConverter = tagSummaryListConverter;
        this.dateFormatterUtility = dateFormatterUtility;
    }

    @Override
    public Article convert(WrapperBodyDataModel<ExtendedEntryDataModel> source) {
        return Article.builder()
                .id(source.getBody().getId())
                .author(createAuthorSummary(source.getBody()))
                .content(source.getBody().getRawContent())
                .creationDate(dateFormatterUtility.formatGeneral(source.getBody().getCreated()))
                .link(source.getBody().getLink())
                .title(source.getBody().getTitle())
                .tags(tagSummaryListConverter.convert(source.getBody().getTags()))
                .attachments(attachmentSummaryListConverter.convert(source.getBody().getAttachments()))
                .build();
    }

    private AuthorSummary createAuthorSummary(EntryDataModel entryDataModel) {
        return new AuthorSummary(entryDataModel.getUser().getUsername());
    }
}
