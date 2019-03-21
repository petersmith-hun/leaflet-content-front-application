package hu.psprog.leaflet.lcfa.core.domain.content;

import hu.psprog.leaflet.lcfa.core.domain.common.PaginationAttributes;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

/**
 * Domain class holding all the necessary information for rendering comments page on user profile.
 *
 * @author Peter Smith
 */
@Getter
@EqualsAndHashCode
@ToString
@Builder
public class UserCommentsPageContent {

    public static final UserCommentsPageContent EMPTY_CONTENT = UserCommentsPageContent.builder().comments(Collections.emptyList()).build();

    private List<CommentSummary> comments;
    private PaginationAttributes paginationAttributes;
}
