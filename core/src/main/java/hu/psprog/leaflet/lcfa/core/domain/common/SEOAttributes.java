package hu.psprog.leaflet.lcfa.core.domain.common;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * SEO attributes domain class.
 *
 * @author Peter Smith
 */
@Getter
@EqualsAndHashCode
@ToString
@Builder
public class SEOAttributes {

    private String pageTitle;
    private String metaTitle;
    private String metaDescription;
    private String metaKeywords;
}
