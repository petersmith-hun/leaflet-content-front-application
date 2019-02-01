package hu.psprog.leaflet.lcfa.core.domain.content;

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

    private AuthorSummary author;
    private String content;
    private String created;
    private boolean createdByArticleAuthor;
}
