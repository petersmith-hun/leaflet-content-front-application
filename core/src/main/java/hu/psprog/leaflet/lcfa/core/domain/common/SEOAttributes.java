package hu.psprog.leaflet.lcfa.core.domain.common;

import lombok.Builder;

/**
 * SEO attributes domain class.
 *
 * @author Peter Smith
 */
@Builder
public record SEOAttributes(
        String pageTitle,
        String metaTitle,
        String metaDescription,
        String metaKeywords
) { }
