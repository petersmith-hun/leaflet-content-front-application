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

    /**
     * Retrieves filtered article list by category ID.
     *
     * @param categoryID category ID to filter by
     * @param page page number of entries
     * @return populated {@link HomePageContent} object
     */
    HomePageContent getArticlesByCategory(long categoryID, int page);

    /**
     * Retrieves filtered article list by tag ID.
     *
     * @param tagID tag ID to filter by
     * @param page page number of entries
     * @return populated {@link HomePageContent} object
     */
    HomePageContent getArticlesByTag(long tagID, int page);

    /**
     * Retrieves filtered article list by content expression (free text).
     *
     * @param contentExpression free text content expression to filter by
     * @param page page number of entries
     * @return populated {@link HomePageContent} object
     */
    HomePageContent getArticlesByContent(String contentExpression, int page);
}
