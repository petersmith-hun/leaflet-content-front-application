package hu.psprog.leaflet.lcfa.core.facade;

import hu.psprog.leaflet.lcfa.core.domain.content.ArticleContent;
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
     * @return populated {@link HomePageContent} object
     */
    HomePageContent getHomePageContent(int page);

    /**
     * Retrieves an article for rendering its content; also returns categories and tags filter data.
     *
     * @param link identifier link of the article
     * @return populated {@link ArticleContent} object
     */
    ArticleContent getArticle(String link);
}
