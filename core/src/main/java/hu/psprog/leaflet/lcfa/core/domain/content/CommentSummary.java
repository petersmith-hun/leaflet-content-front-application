package hu.psprog.leaflet.lcfa.core.domain.content;

import lombok.Builder;

/**
 * Comment summary domain class.
 *
 * @author Peter Smith
 */
@Builder
public record CommentSummary(
        Long id,
        CommentArticleData article,
        AuthorSummary author,
        String content,
        String created,
        boolean enabled,
        boolean deleted,
        boolean createdByArticleAuthor
) { }
