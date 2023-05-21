package hu.psprog.leaflet.lcfa.core.domain.content;

import hu.psprog.leaflet.lcfa.core.domain.common.PaginationAttributes;
import lombok.Builder;

import java.util.List;

/**
 * Domain class holding all the necessary information for home page rendering.
 *
 * @author Peter Smith
 */
@Builder
public record HomePageContent(
        List<EntrySummary> entries,
        List<CategorySummary> categories,
        List<TagSummary> tags,
        PaginationAttributes pagination
) { }
