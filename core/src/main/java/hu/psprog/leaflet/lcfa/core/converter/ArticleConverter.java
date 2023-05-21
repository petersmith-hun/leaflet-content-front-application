package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
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

    private final AttachmentSummaryListConverter attachmentSummaryListConverter;
    private final TagSummaryListConverter tagSummaryListConverter;
    private final DateFormatterUtility dateFormatterUtility;
    private final CategorySummaryConverter categorySummaryConverter;

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
                .id(source.body().id())
                .author(createAuthorSummary(source.body()))
                .content(source.body().rawContent())
                .creationDate(dateFormatterUtility.formatGeneral(extractCreationDate(source.body())))
                .link(source.body().link())
                .title(source.body().title())
                .tags(tagSummaryListConverter.convert(source.body().tags()))
                .attachments(attachmentSummaryListConverter.convert(source.body().attachments()))
                .category(categorySummaryConverter.convert(source.body().category()))
                .build();
    }

    private AuthorSummary createAuthorSummary(ExtendedEntryDataModel entryDataModel) {
        return new AuthorSummary(entryDataModel.user().username());
    }

    private ZonedDateTime extractCreationDate(ExtendedEntryDataModel entryDataModel) {
        return Optional.ofNullable(entryDataModel.published())
                .orElse(entryDataModel.created());
    }
}
