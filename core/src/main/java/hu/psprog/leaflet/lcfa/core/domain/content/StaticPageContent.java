package hu.psprog.leaflet.lcfa.core.domain.content;

import hu.psprog.leaflet.lcfa.core.domain.common.SEOAttributes;
import lombok.Builder;

/**
 * Domain class holding all the necessary information for static page rendering.
 *
 * @author Peter Smith
 */
@Builder
public record StaticPageContent(
        StaticPage page,
        SEOAttributes seo
) { }
