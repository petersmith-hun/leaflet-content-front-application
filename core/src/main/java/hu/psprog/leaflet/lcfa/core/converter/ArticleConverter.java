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

import java.time.ZonedDateTime;
import java.util.Optional;

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
    private CategorySummaryConverter categorySummaryConverter;

    @Autowired
    public ArticleConverter(AttachmentSummaryListConverter attachmentSummaryListConverter, TagSummaryListConverter tagSummaryListConverter,
                            DateFormatterUtility dateFormatterUtility, CategorySummaryConverter categorySummaryConverter) {
        this.attachmentSummaryListConverter = attachmentSummaryListConverter;
        this.tagSummaryListConverter = tagSummaryListConverter;
        this.dateFormatterUtility = dateFormatterUtility;
        this.categorySummaryConverter = categorySummaryConverter;
    }

    @Override
    public Article convert(WrapperBodyDataModel<ExtendedEntryDataModel> source) {
        return Article.builder()
                .id(source.getBody().getId())
                .author(createAuthorSummary(source.getBody()))
                .content(source.getBody().getRawContent())
                .creationDate(dateFormatterUtility.formatGeneral(extractCreationDate(source.getBody())))
                .link(source.getBody().getLink())
                .title(source.getBody().getTitle())
                .tags(tagSummaryListConverter.convert(source.getBody().getTags()))
                .attachments(attachmentSummaryListConverter.convert(source.getBody().getAttachments()))
                .category(categorySummaryConverter.convert(source.getBody().getCategory()))
                .build();
    }

    private AuthorSummary createAuthorSummary(EntryDataModel entryDataModel) {
        return new AuthorSummary(entryDataModel.getUser().getUsername());
    }

    private ZonedDateTime extractCreationDate(EntryDataModel entryDataModel) {
        return Optional.ofNullable(entryDataModel.getPublished())
                .orElse(entryDataModel.getCreated());
    }
}
