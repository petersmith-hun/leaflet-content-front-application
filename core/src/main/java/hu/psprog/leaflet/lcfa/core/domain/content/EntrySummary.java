package hu.psprog.leaflet.lcfa.core.domain.content;

import lombok.Builder;

/**
 * Entry summary domain class.
 *
 * @author Peter Smith
 */
@Builder
public record EntrySummary(
        String title,
        String link,
        AuthorSummary author,
        String creationDate,
        String prologue
) { }
