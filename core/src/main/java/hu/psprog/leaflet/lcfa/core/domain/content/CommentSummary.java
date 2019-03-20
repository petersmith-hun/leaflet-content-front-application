package hu.psprog.leaflet.lcfa.core.domain.content;

import hu.psprog.leaflet.lcfa.core.converter.CommentArticleData;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Comment summary domain class.
 *
 * @author Peter Smith
 */
@Getter
@EqualsAndHashCode
@ToString
@Builder
public class CommentSummary {

    private Long id;
    private CommentArticleData article;
    private AuthorSummary author;
    private String content;
    private String created;
    private boolean enabled;
    private boolean deleted;
    private boolean createdByArticleAuthor;
}
