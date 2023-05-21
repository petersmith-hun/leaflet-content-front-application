package hu.psprog.leaflet.lcfa.core.domain.content;

import lombok.Builder;

/**
 * Contact page domain class.
 *
 * @author Peter Smith
 */
@Builder
public record ContactPageContent(
        StaticPageContent contactInfo
) { }
