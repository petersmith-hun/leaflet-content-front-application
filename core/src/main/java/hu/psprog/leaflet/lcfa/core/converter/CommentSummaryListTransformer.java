package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.api.rest.response.comment.CommentDataModel;
import hu.psprog.leaflet.api.rest.response.comment.CommentListDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryDataModel;
import hu.psprog.leaflet.lcfa.core.domain.content.AuthorSummary;
import hu.psprog.leaflet.lcfa.core.domain.content.CommentSummary;
import hu.psprog.leaflet.lcfa.core.formatter.DateFormatterUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Converts {@link List} of {@link CommentDataModel} objects to {@link List} of {@link CommentSummary} objects.
 * Checks whether a comment is created by the same user as the one who created the given entry.
 *
 * @author Peter Smith
 */
@Component
public class CommentSummaryListTransformer {

    private DateFormatterUtility dateFormatterUtility;

    @Autowired
    public CommentSummaryListTransformer(DateFormatterUtility dateFormatterUtility) {
        this.dateFormatterUtility = dateFormatterUtility;
    }

    public List<CommentSummary> convert(CommentListDataModel source, EntryDataModel entryDataModel) {
        return source.getComments().stream()
                .map(commentDataModel -> convert(commentDataModel, entryDataModel))
                .collect(Collectors.toList());
    }

    private CommentSummary convert(CommentDataModel source, EntryDataModel entryDataModel) {
        return CommentSummary.builder()
                .author(createAuthorSummary(source))
                .content(source.getContent())
                .created(dateFormatterUtility.formatComments(source.getCreated()))
                .createdByArticleAuthor(isCreatedByArticleAuthor(source, entryDataModel))
                .build();
    }

    private AuthorSummary createAuthorSummary(CommentDataModel commentDataModel) {
        return new AuthorSummary(commentDataModel.getOwner().getUsername());
    }

    private boolean isCreatedByArticleAuthor(CommentDataModel source, EntryDataModel entryDataModel) {
        return entryDataModel.getUser().getId() == source.getOwner().getId();
    }
}
