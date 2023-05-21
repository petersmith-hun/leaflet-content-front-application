package hu.psprog.leaflet.lcfa.core.domain.common;

import lombok.Builder;

/**
 * Pagination attributes domain class.
 *
 * @author Peter Smith
 */
@Builder
public record PaginationAttributes(
        int pageCount,
        int pageNumber,
        boolean hasNext,
        boolean hasPrevious
) { }
