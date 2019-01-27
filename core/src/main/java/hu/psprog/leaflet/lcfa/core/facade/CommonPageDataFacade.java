package hu.psprog.leaflet.lcfa.core.facade;

import hu.psprog.leaflet.lcfa.core.domain.common.CommonPageData;

/**
 * Facade for common page data retrieval operations.
 *
 * @author Peter Smith
 */
public interface CommonPageDataFacade {

    /**
     * Retrieves common page data.
     * Shall be used for initialization of common page data cache.
     *
     * @return populated {@link CommonPageData}
     */
    CommonPageData getCommonPageData();
}
