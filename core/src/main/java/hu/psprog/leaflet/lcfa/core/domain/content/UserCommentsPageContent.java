package hu.psprog.leaflet.lcfa.core.domain.content;

import hu.psprog.leaflet.lcfa.core.domain.common.PaginationAttributes;
import lombok.Builder;

import java.util.Collections;
import java.util.List;

/**
 * Domain class holding all the necessary information for rendering comments page on user profile.
 *
 * @author Peter Smith
 */
@Builder
public record UserCommentsPageContent(
        List<CommentSummary> comments,
        PaginationAttributes paginationAttributes
) {

    public static final UserCommentsPageContent EMPTY_CONTENT = UserCommentsPageContent.builder().comments(Collections.emptyList()).build();
}
