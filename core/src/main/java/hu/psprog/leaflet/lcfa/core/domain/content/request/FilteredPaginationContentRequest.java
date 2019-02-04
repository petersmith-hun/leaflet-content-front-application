package hu.psprog.leaflet.lcfa.core.domain.content.request;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Request domain class for pageable and filtered content requests.
 *
 * @author Peter Smith
 */
@Getter
@EqualsAndHashCode
@ToString
@Builder
public class FilteredPaginationContentRequest<T> {

    private T filterValue;
    private int page;
    private int limit;
    private OrderBy.Entry entryOrderBy;
    private OrderDirection entryOrderDirection;
}
