package hu.psprog.leaflet.lcfa.web.ui.support.navigation;

import hu.psprog.leaflet.lcfa.web.model.NavigationItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static hu.psprog.leaflet.lcfa.web.controller.BaseController.PATH_PROFILE;
import static hu.psprog.leaflet.lcfa.web.controller.BaseController.PATH_PROFILE_MY_COMMENTS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link AccountNavigationBarSupport}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class AccountNavigationBarSupportTest {

    @InjectMocks
    private AccountNavigationBarSupport accountNavigationBarSupport;

    @Test
    public void shouldReturnProfileNavigationBar() {

        // when
        List<NavigationItem> result = accountNavigationBarSupport.profile();

        // then
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));
        assertProfileNavigationItem(result);
    }

    @Test
    public void shouldReturnMyCommentsNavigationBar() {

        // when
        List<NavigationItem> result = accountNavigationBarSupport.myComments();

        // then
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(2));
        assertProfileNavigationItem(result);
        assertMyCommentNavigationItem(result);
    }

    private void assertProfileNavigationItem(List<NavigationItem> result) {
        assertThat(result.getFirst().getTitle(), equalTo("section.profile.navigation.baseinfo"));
        assertThat(result.getFirst().getLink(), equalTo(PATH_PROFILE));
        assertThat(result.getFirst().isTranslatable(), is(true));
    }

    private void assertMyCommentNavigationItem(List<NavigationItem> result) {
        assertThat(result.get(1).getTitle(), equalTo("section.profile.navigation.comments"));
        assertThat(result.get(1).getLink(), equalTo(PATH_PROFILE_MY_COMMENTS));
        assertThat(result.get(1).isTranslatable(), is(true));
    }
}
