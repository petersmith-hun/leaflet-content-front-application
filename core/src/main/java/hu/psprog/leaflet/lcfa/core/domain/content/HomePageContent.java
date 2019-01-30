package hu.psprog.leaflet.lcfa.core.domain.content;

import hu.psprog.leaflet.lcfa.core.domain.common.PaginationAttributes;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * Domain class holding all the necessary information for home page rendering.
 *
 * @author Peter Smith
 */
@Getter
@EqualsAndHashCode
@ToString
@Builder
public class HomePageContent {

    private List<EntrySummary> entries;
    private List<CategorySummary> categories;
    private List<TagSummary> tags;
    private PaginationAttributes pagination;
}
