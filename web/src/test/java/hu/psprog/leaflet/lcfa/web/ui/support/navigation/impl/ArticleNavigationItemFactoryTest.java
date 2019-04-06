package hu.psprog.leaflet.lcfa.web.ui.support.navigation.impl;

import hu.psprog.leaflet.lcfa.core.domain.content.Article;
import hu.psprog.leaflet.lcfa.web.model.NavigationItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link ArticleNavigationItemFactory}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class ArticleNavigationItemFactoryTest {

    private static final String ARTICLE_TITLE = "Article title";
    private static final Article ARTICLE = Article.builder()
            .link("article-link")
            .title(ARTICLE_TITLE)
            .build();
    private static final NavigationItem EXPECTED_NAVIGATION_ITEM = NavigationItem.build("/article/article-link", ARTICLE_TITLE);

    @InjectMocks
    private ArticleNavigationItemFactory articleNavigationItemFactory;

    @Test
    public void shouldCreateNavigationItem() {

        // when
        NavigationItem result = articleNavigationItemFactory.create(ARTICLE);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(EXPECTED_NAVIGATION_ITEM));
    }

    @Test
    public void shouldForModelClassReturnClassInstance() {

        // when
        Class<Article> result = articleNavigationItemFactory.forModelClass();

        // then
        assertThat(result, notNullValue());
    }
}
