package hu.psprog.leaflet.lcfa.core.domain.content.request;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Request domain class for pageable and filtered content requests.
 *
 * @param <T> type of filtering parameter
 * @param <BY> enum type of order by parameter
 * @author Peter Smith
 */
@Getter
@EqualsAndHashCode
@ToString
@Builder
public class FilteredPaginationContentRequest<T, BY extends Enum<BY>> {

    private T filterValue;
    private int page;
    private int limit;
    private BY orderBy;
    private OrderDirection orderDirection;
}
