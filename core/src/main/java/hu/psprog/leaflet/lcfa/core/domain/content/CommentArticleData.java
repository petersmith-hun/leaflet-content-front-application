package hu.psprog.leaflet.lcfa.core.domain.content;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Domain class holding basic information of the entry assigned to a comment.
 *
 * @author Peter Smith
 */
@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class CommentArticleData {

    private String title;
    private String link;
}
