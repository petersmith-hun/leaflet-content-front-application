package hu.psprog.leaflet.lcfa.core.facade;

import hu.psprog.leaflet.lcfa.core.domain.content.HomePageContent;

/**
 * Facade for blog content retrieval operations.
 *
 * @author Peter Smith
 */
public interface BlogContentFacade {

    /**
     * Retrieves contents of the home page.
     *
     * @param page page number of entries
     * @return popualed {@link HomePageContent} object
     */
    HomePageContent getHomePageContent(int page);
}
