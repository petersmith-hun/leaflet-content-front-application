package hu.psprog.leaflet.lcfa.web.ui.support.navigation;

import hu.psprog.leaflet.lcfa.web.model.NavigationItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static hu.psprog.leaflet.lcfa.web.controller.BaseController.PATH_PROFILE;
import static hu.psprog.leaflet.lcfa.web.controller.BaseController.PATH_PROFILE_CHANGE_PASSWORD;
import static hu.psprog.leaflet.lcfa.web.controller.BaseController.PATH_PROFILE_DELETE_ACCOUNT;
import static hu.psprog.leaflet.lcfa.web.controller.BaseController.PATH_PROFILE_MY_COMMENTS;
import static hu.psprog.leaflet.lcfa.web.controller.BaseController.PATH_SIGN_IN;
import static hu.psprog.leaflet.lcfa.web.controller.BaseController.PATH_SIGN_UP;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link AccountNavigationBarSupport}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
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
    public void shouldReturnPasswordChangeNavigationBar() {

        // when
        List<NavigationItem> result = accountNavigationBarSupport.passwordChange();

        // then
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(2));
        assertProfileNavigationItem(result);
        assertPasswordChangeNavigationItem(result);
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

    @Test
    public void shouldReturnAccountDeletionNavigationBar() {

        // when
        List<NavigationItem> result = accountNavigationBarSupport.accountDeletion();

        // then
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(2));
        assertProfileNavigationItem(result);
        assertAccountDeletionNavigationItem(result);
    }

    @Test
    public void shouldReturnSignInNavigationBar() {

        // when
        List<NavigationItem> result = accountNavigationBarSupport.signIn();

        // then
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));
        assertSignInNavigationItem(result);
    }

    @Test
    public void shouldReturnSignUpNavigationBar() {

        // when
        List<NavigationItem> result = accountNavigationBarSupport.signUp();

        // then
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));
        assertSignUpNavigationItem(result);
    }

    @Test
    public void shouldReturnPasswordResetNavigationBar() {

        // when
        List<NavigationItem> result = accountNavigationBarSupport.passwordReset();

        // then
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));
        assertPasswordResetNavigationItem(result);
    }

    private void assertProfileNavigationItem(List<NavigationItem> result) {
        assertThat(result.get(0).getTitle(), equalTo("section.profile.navigation.baseinfo"));
        assertThat(result.get(0).getLink(), equalTo(PATH_PROFILE));
        assertThat(result.get(0).isTranslatable(), is(true));
    }

    private void assertPasswordChangeNavigationItem(List<NavigationItem> result) {
        assertThat(result.get(1).getTitle(), equalTo("section.profile.navigation.pwchange"));
        assertThat(result.get(1).getLink(), equalTo(PATH_PROFILE_CHANGE_PASSWORD));
        assertThat(result.get(1).isTranslatable(), is(true));
    }

    private void assertMyCommentNavigationItem(List<NavigationItem> result) {
        assertThat(result.get(1).getTitle(), equalTo("section.profile.navigation.comments"));
        assertThat(result.get(1).getLink(), equalTo(PATH_PROFILE_MY_COMMENTS));
        assertThat(result.get(1).isTranslatable(), is(true));
    }

    private void assertAccountDeletionNavigationItem(List<NavigationItem> result) {
        assertThat(result.get(1).getTitle(), equalTo("section.profile.navigation.delete"));
        assertThat(result.get(1).getLink(), equalTo(PATH_PROFILE_DELETE_ACCOUNT));
        assertThat(result.get(1).isTranslatable(), is(true));
    }

    private void assertSignInNavigationItem(List<NavigationItem> result) {
        assertThat(result.get(0).getTitle(), equalTo("menu.sign_in"));
        assertThat(result.get(0).getLink(), equalTo(PATH_SIGN_IN));
        assertThat(result.get(0).isTranslatable(), is(true));
    }

    private void assertSignUpNavigationItem(List<NavigationItem> result) {
        assertThat(result.get(0).getTitle(), equalTo("menu.sign_up"));
        assertThat(result.get(0).getLink(), equalTo(PATH_SIGN_UP));
        assertThat(result.get(0).isTranslatable(), is(true));
    }

    private void assertPasswordResetNavigationItem(List<NavigationItem> result) {
        assertThat(result.get(0).getTitle(), equalTo("form.pwreset.title"));
        assertThat(result.get(0).getLink(), equalTo("''"));
        assertThat(result.get(0).isTranslatable(), is(true));
    }
}
