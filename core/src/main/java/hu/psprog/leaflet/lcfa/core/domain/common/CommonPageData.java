package hu.psprog.leaflet.lcfa.core.domain.common;

import hu.psprog.leaflet.lcfa.core.domain.content.EntrySummary;
import lombok.Builder;

import java.util.List;

/**
 * Common page date container domain class.
 * Contains SEO information, menus, and the latest entries.
 *
 * @author Peter Smith
 */
@Builder
public record CommonPageData(
        SEOAttributes seo,
        List<MenuItem> headerMenu,
        List<MenuItem> footerMenu,
        List<MenuItem> standaloneMenuItems,
        List<EntrySummary> latestEntries,
        MenuItem loginMenuItem
) { }
