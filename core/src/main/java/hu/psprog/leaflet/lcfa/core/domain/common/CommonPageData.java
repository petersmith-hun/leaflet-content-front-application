package hu.psprog.leaflet.lcfa.core.domain.common;

import hu.psprog.leaflet.lcfa.core.domain.content.EntrySummary;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * Common page date container domain class.
 * Contains SEO information, menus, and the latest entries.
 *
 * @author Peter Smith
 */
@Getter
@EqualsAndHashCode
@ToString
@Builder
public class CommonPageData {

    private SEOAttributes seo;
    private List<MenuItem> headerMenu;
    private List<MenuItem> footerMenu;
    private List<MenuItem> standaloneMenuItems;
    private List<EntrySummary> latestEntries;
}
