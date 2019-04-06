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
 * Unit tests for {@link SimpleSamePageNavigationItemFactory}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class SimpleSamePageNavigationItemFactoryTest {

    private static final String TITLE = "Navigation item title";
    private static final NavigationItem EXPECTED_NAVIGATION_ITEM = NavigationItem.build("''", TITLE);

    @InjectMocks
    private SimpleSamePageNavigationItemFactory simpleSamePageNavigationItemFactory;

    @Test
    public void shouldCreateNavigationItem() {

        // when
        NavigationItem result = simpleSamePageNavigationItemFactory.create(TITLE);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(EXPECTED_NAVIGATION_ITEM));
    }

    @Test
    public void shouldForModelClassReturnClassInstance() {

        // when
        Class<String> result = simpleSamePageNavigationItemFactory.forModelClass();

        // then
        assertThat(result, notNullValue());
    }
}
