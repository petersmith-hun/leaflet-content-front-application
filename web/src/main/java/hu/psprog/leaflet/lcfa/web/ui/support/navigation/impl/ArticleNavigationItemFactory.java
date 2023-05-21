package hu.psprog.leaflet.lcfa.web.ui.support.navigation.impl;

import hu.psprog.leaflet.lcfa.core.domain.content.Article;
import hu.psprog.leaflet.lcfa.web.controller.BaseController;
import hu.psprog.leaflet.lcfa.web.model.NavigationItem;
import hu.psprog.leaflet.lcfa.web.ui.support.navigation.NavigationItemFactory;
import org.springframework.stereotype.Component;

/**
 * {@link NavigationItemFactory} implementation for generating navigation bar items based on {@link Article} objects.
 *
 * @author Peter Smith
 */
@Component
public class ArticleNavigationItemFactory implements NavigationItemFactory<Article> {

    private static final String LINK = "{link}";

    @Override
    public NavigationItem create(Article sourceModel) {
        return NavigationItem.build(createLink(sourceModel), sourceModel.title());
    }

    @Override
    public Class<Article> forModelClass() {
        return Article.class;
    }

    private String createLink(Article article) {
        return BaseController.PATH_ARTICLE_BY_LINK
                .replace(LINK, article.link());
    }
}
