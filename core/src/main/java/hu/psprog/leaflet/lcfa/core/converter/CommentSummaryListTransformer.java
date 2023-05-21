package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.api.rest.response.comment.CommentData;
import hu.psprog.leaflet.api.rest.response.comment.CommentDataModel;
import hu.psprog.leaflet.api.rest.response.comment.CommentListDataModel;
import hu.psprog.leaflet.api.rest.response.comment.ExtendedCommentDataModel;
import hu.psprog.leaflet.api.rest.response.comment.ExtendedCommentListDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryData;
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

    private final DateFormatterUtility dateFormatterUtility;

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
        return convert(source.comments(), null);
    }

    /**
     * Converts an {@link ExtendedCommentListDataModel} to a {@link List} of {@link CommentSummary} objects.
     * Includes article information as {@link CommentArticleData}.
     *
     * @param source source of type {@link CommentListDataModel}
     * @return converted {@link List} of {@link CommentSummary} objects.
     */
    public List<CommentSummary> convert(ExtendedCommentListDataModel source) {
        return convert(source.comments(), null);
    }

    /**
     * Converts a {@link CommentListDataModel} to a {@link List} of {@link CommentSummary} objects.
     * Also checks if the given comment is created by the same user as the provided article.
     *
     * @param source source of type {@link CommentListDataModel}
     * @param entryData {@link EntryData} object to check if the author is the same.
     * @return converted {@link List} of {@link CommentSummary} objects.
     */
    public List<CommentSummary> convert(List<? extends CommentData> source, EntryData entryData) {

        return source.stream()
                .map(commentData -> convert(commentData, entryData))
                .collect(Collectors.toList());
    }

    private CommentSummary convert(CommentData source, EntryData entryData) {

        return CommentSummary.builder()
                .id(source.id())
                .author(createAuthorSummary(source))
                .content(source.content())
                .created(dateFormatterUtility.formatComments(source.created()))
                .enabled(source.enabled())
                .deleted(source.deleted())
                .createdByArticleAuthor(isCreatedByArticleAuthor(source, entryData))
                .article(createArticleDataIfPresent(source))
                .build();
    }

    private AuthorSummary createAuthorSummary(CommentData commentData) {
        return new AuthorSummary(commentData.owner().username());
    }

    private boolean isCreatedByArticleAuthor(CommentData source, EntryData entryData) {

        return Optional.ofNullable(entryData)
                .map(entry -> entry.user().id() == source.owner().id())
                .orElse(false);
    }

    private CommentArticleData createArticleDataIfPresent(CommentData source) {

        CommentArticleData commentArticleData = null;
        if (source instanceof ExtendedCommentDataModel extendedCommentDatamodel) {
            EntryDataModel entry = extendedCommentDatamodel.associatedEntry();
            commentArticleData = new CommentArticleData(entry.title(), entry.link());
        }

        return commentArticleData;
    }
}
