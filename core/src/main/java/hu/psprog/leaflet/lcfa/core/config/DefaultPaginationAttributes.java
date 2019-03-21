package hu.psprog.leaflet.lcfa.core.config;

import hu.psprog.leaflet.lcfa.core.domain.content.request.OrderDirection;
import lombok.Data;

/**
 * Domain class for default pagination settings.
 *
 * @param <T> order by parameter enum type
 * @author Peter Smith
 */
@Data
public class DefaultPaginationAttributes<T extends Enum<T>> {

    private int limit;
    private T orderBy;
    private OrderDirection orderDirection;
}
