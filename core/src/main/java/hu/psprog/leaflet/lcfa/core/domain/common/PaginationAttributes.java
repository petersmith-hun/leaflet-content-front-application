package hu.psprog.leaflet.lcfa.core.domain.common;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Pagination attributes domain class.
 *
 * @author Peter Smith
 */
@Getter
@EqualsAndHashCode
@ToString
@Builder
public class PaginationAttributes {

    private int pageCount;
    private int pageNumber;
    private boolean hasNext;
    private boolean hasPrevious;
}
