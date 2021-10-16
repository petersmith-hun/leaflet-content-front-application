package hu.psprog.leaflet.lcfa.web.ui.support.navigation.impl;

import hu.psprog.leaflet.lcfa.core.domain.content.Article;
import hu.psprog.leaflet.lcfa.core.domain.content.CategorySummary;
import hu.psprog.leaflet.lcfa.web.ui.support.navigation.NavigationItemFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
@ExtendWith(MockitoExtension.class)
public class NavigationItemFactoryRegistryTest {

    @Mock
    private NavigationItemFactory<Article> articleNavigationItemFactory;

    @Mock
    private NavigationItemFactory<CategorySummary> categorySummaryNavigationItemFactory;

    private NavigationItemFactoryRegistry navigationItemFactoryRegistry;

    @BeforeEach
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

    @Test
    public void shouldGetFactoryThrowExceptionForUnknownModelClass() {

        // when
        Assertions.assertThrows(IllegalArgumentException.class, () -> navigationItemFactoryRegistry.getFactory(String.class));

        // then
        // exception expected
    }
}
