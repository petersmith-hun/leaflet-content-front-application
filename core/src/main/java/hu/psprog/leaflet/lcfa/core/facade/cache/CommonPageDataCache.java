package hu.psprog.leaflet.lcfa.core.facade.cache;

import hu.psprog.leaflet.lcfa.core.domain.common.CommonPageData;

import java.util.Optional;

/**
 * Implementations should be used to cache common page data information.
 * {@link CommonPageData} is enough to be updated once in a while, as these parameters are not expected to be changed regularly.
 *
 * @author Peter Smith
 */
public interface CommonPageDataCache {

    /**
     * Returns cached {@link CommonPageData} object, if available.
     *
     * @return stored {@link CommonPageData} wrapped as {@link Optional}
     */
    Optional<CommonPageData> getCached();

    /**
     * Forces the cache to update stored {@link CommonPageData} object.
     *
     * @param commonPageData new {@link CommonPageData} for the cache to be updated with
     */
    void update(CommonPageData commonPageData);
}
