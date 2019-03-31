package hu.psprog.leaflet.lcfa.web.ui.support.navigation.impl;

import hu.psprog.leaflet.lcfa.core.domain.content.Article;
import hu.psprog.leaflet.lcfa.core.domain.content.CategorySummary;
import hu.psprog.leaflet.lcfa.web.ui.support.navigation.NavigationItemFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link NavigationItemFactoryRegistry}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class NavigationItemFactoryRegistryTest {

    @Mock
    private NavigationItemFactory<Article> articleNavigationItemFactory;

    @Mock
    private NavigationItemFactory<CategorySummary> categorySummaryNavigationItemFactory;

    private NavigationItemFactoryRegistry navigationItemFactoryRegistry;

    @Before
    public void setup() {
        given(articleNavigationItemFactory.forModelClass()).willReturn(Article.class);
        given(categorySummaryNavigationItemFactory.forModelClass()).willReturn(CategorySummary.class);
        navigationItemFactoryRegistry = new NavigationItemFactoryRegistry(Arrays.asList(categorySummaryNavigationItemFactory, articleNavigationItemFactory));
    }

    @Test
    public void shouldGetFactoryForArticleClass() {

        // when
        NavigationItemFactory<Article> result = navigationItemFactoryRegistry.getFactory(Article.class);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(articleNavigationItemFactory));
        verify(categorySummaryNavigationItemFactory).forModelClass();
        verify(articleNavigationItemFactory).forModelClass();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldGetFactoryThrowExceptionForUnknownModelClass() {

        // when
        navigationItemFactoryRegistry.getFactory(String.class);

        // then
        // exception expected
    }
}
