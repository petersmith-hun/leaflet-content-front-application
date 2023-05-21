package hu.psprog.leaflet.lcfa.core.domain.content;

/**
 * Domain class holding basic information of the entry assigned to a comment.
 *
 * @author Peter Smith
 */
public record CommentArticleData(
        String title,
        String link
) { }
