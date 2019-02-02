package hu.psprog.leaflet.lcfa.core.facade;

import hu.psprog.leaflet.lcfa.core.domain.content.StaticPageContent;
import hu.psprog.leaflet.lcfa.core.domain.content.StaticPageType;

/**
 * Facade for static page retrieval operations.
 *
 * @author Peter Smith
 */
public interface StaticPageContentFacade {

    /**
     * Returns static page identified by given type.
     *
     * @param staticPageType type of static page to retrieve
     * @return populated {@link StaticPageContent} object
     */
    StaticPageContent getStaticPage(StaticPageType staticPageType);
}
