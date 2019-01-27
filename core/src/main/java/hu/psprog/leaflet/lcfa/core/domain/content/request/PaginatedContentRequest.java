package hu.psprog.leaflet.lcfa.core.domain.content.request;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Request domain class for pageable content requests.
 *
 * @author Peter Smith
 */
@Getter
@EqualsAndHashCode
@ToString
@Builder
public class PaginatedContentRequest {

    private int page;
    private int limit;
    private OrderBy.Entry entryOrderBy;
    private OrderDirection entryOrderDirection;
}
