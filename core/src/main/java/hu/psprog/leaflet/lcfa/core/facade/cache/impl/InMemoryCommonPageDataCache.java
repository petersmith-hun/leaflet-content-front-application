package hu.psprog.leaflet.lcfa.core.facade.cache.impl;

import hu.psprog.leaflet.lcfa.core.domain.common.CommonPageData;
import hu.psprog.leaflet.lcfa.core.facade.cache.CommonPageDataCache;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;

/**
 * {@link CommonPageDataCache} implementation storing common page data in memory.
 * Cache is considered to be invalid in two cases:
 *  - stored instance is null (obviously)
 *  - cache is outdated (last update occurred before the specified interval)
 * As a safety measurement, in case the stored instance is present, but last update
 * date is not specified, cache is also considered to be invalid.
 *
 * @author Peter Smith
 */
@Component
@ConfigurationProperties(prefix = "page-config.common-page-data-cache")
public class InMemoryCommonPageDataCache implements CommonPageDataCache {

    private long cacheTimeout;
    private ChronoUnit cacheTimeoutUnit;

    private ZonedDateTime lastUpdate = null;
    private CommonPageData cachedCommonPageData = null;

    @Override
    public Optional<CommonPageData> getCached() {

        CommonPageData commonPageData = null;
        if (!updatedNeeded()) {
            commonPageData = cachedCommonPageData;
        }

        return Optional.ofNullable(commonPageData);
    }

    @Override
    public void update(CommonPageData commonPageData) {
        cachedCommonPageData = commonPageData;
        lastUpdate = ZonedDateTime.now();
    }

    public void setCacheTimeout(long cacheTimeout) {
        this.cacheTimeout = cacheTimeout;
    }

    public void setCacheTimeoutUnit(ChronoUnit cacheTimeoutUnit) {
        this.cacheTimeoutUnit = cacheTimeoutUnit;
    }

    private boolean updatedNeeded() {
        return Objects.isNull(cachedCommonPageData)
                || Objects.isNull(lastUpdate)
                || isOutdated();
    }

    private boolean isOutdated() {
        return ZonedDateTime.now()
                .minus(cacheTimeout, cacheTimeoutUnit)
                .isAfter(lastUpdate);
    }
}
