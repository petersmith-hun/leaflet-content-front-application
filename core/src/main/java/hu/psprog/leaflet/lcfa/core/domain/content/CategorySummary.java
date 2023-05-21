package hu.psprog.leaflet.lcfa.core.domain.content;

import lombok.Builder;

/**
 * Category summary domain class.
 *
 * @author Peter Smith
 */
@Builder
public record CategorySummary(
        Long id,
        String title,
        String alias
) { }
