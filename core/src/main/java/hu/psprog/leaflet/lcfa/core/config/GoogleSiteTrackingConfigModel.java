package hu.psprog.leaflet.lcfa.core.config;

import lombok.Data;

/**
 * Configuration model for Google Site Tracking (used in "page-config" section).
 *
 * @author Peter Smith
 */
@Data
public class GoogleSiteTrackingConfigModel {

    private String siteKey;
    private String scriptSource;
}
