package hu.psprog.leaflet.lcfa.core.config;

import lombok.Data;

import java.time.temporal.ChronoUnit;

/**
 * Config parameter model for "page-config.common-page-data-cache" configuration section.
 *
 * @author Peter Smith
 */
@Data
public class CommonPageDataCacheConfigModel {

    private int latestEntriesCount;
    private long cacheTimeout;
    private ChronoUnit cacheTimeoutUnit;
}
