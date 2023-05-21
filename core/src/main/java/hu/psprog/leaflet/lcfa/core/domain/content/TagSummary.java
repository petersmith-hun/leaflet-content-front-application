package hu.psprog.leaflet.lcfa.core.domain.content;

import lombok.Builder;

/**
 * Tag summary domain class.
 *
 * @author Peter Smith
 */
@Builder
public record TagSummary(
        Long id,
        String name,
        String alias
) { }
