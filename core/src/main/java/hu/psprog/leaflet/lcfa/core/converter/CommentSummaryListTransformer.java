package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.api.rest.response.comment.CommentDataModel;
import hu.psprog.leaflet.api.rest.response.comment.CommentListDataModel;
import hu.psprog.leaflet.api.rest.response.comment.ExtendedCommentDataModel;
import hu.psprog.leaflet.api.rest.response.comment.ExtendedCommentListDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryDataModel;
import hu.psprog.leaflet.lcfa.core.domain.content.AuthorSummary;
import hu.psprog.leaflet.lcfa.core.domain.content.CommentArticleData;
import hu.psprog.leaflet.lcfa.core.domain.content.CommentSummary;
import hu.psprog.leaflet.lcfa.core.formatter.DateFormatterUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
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

    /**
     * Converts a {@link CommentListDataModel} to a {@link List} of {@link CommentSummary} objects.
     *
     * @param source source of type {@link CommentListDataModel}
     * @return converted {@link List} of {@link CommentSummary} objects.
     */
    public List<CommentSummary> convert(CommentListDataModel source) {
        return convert(source.getComments(), null);
    }

    /**
     * Converts an {@link ExtendedCommentListDataModel} to a {@link List} of {@link CommentSummary} objects.
     * Includes article information as {@link CommentArticleData}.
     *
     * @param source source of type {@link CommentListDataModel}
     * @return converted {@link List} of {@link CommentSummary} objects.
     */
    public List<CommentSummary> convert(ExtendedCommentListDataModel source) {
        return convert(source.getComments(), null);
    }

    /**
     * Converts a {@link CommentListDataModel} to a {@link List} of {@link CommentSummary} objects.
     * Also checks if the given comment is created by the same user as the provided article.
     *
     * @param source source of type {@link CommentListDataModel}
     * @param entryDataModel {@link EntryDataModel} object to check if the author is the same.
     * @return converted {@link List} of {@link CommentSummary} objects.
     */
    public List<CommentSummary> convert(List<? extends CommentDataModel> source, EntryDataModel entryDataModel) {
        return source.stream()
                .map(commentDataModel -> convert(commentDataModel, entryDataModel))
                .collect(Collectors.toList());
    }

    private CommentSummary convert(CommentDataModel source, EntryDataModel entryDataModel) {

        CommentSummary.CommentSummaryBuilder builder = CommentSummary.builder()
                .id(source.getId())
                .author(createAuthorSummary(source))
                .content(source.getContent())
                .created(dateFormatterUtility.formatComments(source.getCreated()))
                .enabled(source.isEnabled())
                .deleted(source.isDeleted())
                .createdByArticleAuthor(isCreatedByArticleAuthor(source, entryDataModel));

        if (source instanceof ExtendedCommentDataModel) {
            EntryDataModel entry = ((ExtendedCommentDataModel) source).getAssociatedEntry();
            builder.article(new CommentArticleData(entry.getTitle(), entry.getLink()));
        }

        return builder.build();
    }

    private AuthorSummary createAuthorSummary(CommentDataModel commentDataModel) {
        return new AuthorSummary(commentDataModel.getOwner().getUsername());
    }

    private boolean isCreatedByArticleAuthor(CommentDataModel source, EntryDataModel entryDataModel) {
        return Optional.ofNullable(entryDataModel)
                .map(entry -> entry.getUser().getId() == source.getOwner().getId())
                .orElse(false);
    }
}
