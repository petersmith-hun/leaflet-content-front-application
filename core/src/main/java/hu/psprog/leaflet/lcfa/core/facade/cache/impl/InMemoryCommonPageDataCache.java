package hu.psprog.leaflet.lcfa.core.facade.cache.impl;

import hu.psprog.leaflet.lcfa.core.config.CommonPageDataCacheConfigModel;
import hu.psprog.leaflet.lcfa.core.config.PageConfigModel;
import hu.psprog.leaflet.lcfa.core.domain.common.CommonPageData;
import hu.psprog.leaflet.lcfa.core.facade.cache.CommonPageDataCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
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
public class InMemoryCommonPageDataCache implements CommonPageDataCache {

    private final CommonPageDataCacheConfigModel cacheConfig;
    private ZonedDateTime lastUpdate = null;
    private CommonPageData cachedCommonPageData = null;

    @Autowired
    public InMemoryCommonPageDataCache(PageConfigModel pageConfig) {
        this.cacheConfig = pageConfig.getCommonPageDataCache();
    }

    @Override
    public Optional<CommonPageData> getCached() {

        CommonPageData commonPageData = null;
        if (!updateNeeded()) {
            commonPageData = cachedCommonPageData;
        }

        return Optional.ofNullable(commonPageData);
    }

    @Override
    public void update(CommonPageData commonPageData) {
        cachedCommonPageData = commonPageData;
        lastUpdate = ZonedDateTime.now();
    }

    private boolean updateNeeded() {
        return Objects.isNull(cachedCommonPageData)
                || Objects.isNull(lastUpdate)
                || isOutdated();
    }

    private boolean isOutdated() {
        return ZonedDateTime.now()
                .minus(cacheConfig.getCacheTimeout(), cacheConfig.getCacheTimeoutUnit())
                .isAfter(lastUpdate);
    }
}
