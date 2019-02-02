package hu.psprog.leaflet.lcfa.core.domain.content;

import hu.psprog.leaflet.lcfa.core.domain.common.SEOAttributes;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Domain class holding all the necessary information for static page rendering.
 *
 * @author Peter Smith
 */
@Getter
@EqualsAndHashCode
@ToString
@Builder
public class StaticPageContent {

    private StaticPage page;
    private SEOAttributes seo;
}
