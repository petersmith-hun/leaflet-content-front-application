package hu.psprog.leaflet.lcfa.web.ui.support.navigation;

import hu.psprog.leaflet.lcfa.web.model.NavigationItem;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static hu.psprog.leaflet.lcfa.web.controller.BaseController.PATH_PROFILE;
import static hu.psprog.leaflet.lcfa.web.controller.BaseController.PATH_PROFILE_CHANGE_PASSWORD;
import static hu.psprog.leaflet.lcfa.web.controller.BaseController.PATH_PROFILE_DELETE_ACCOUNT;
import static hu.psprog.leaflet.lcfa.web.controller.BaseController.PATH_PROFILE_MY_COMMENTS;
import static hu.psprog.leaflet.lcfa.web.controller.BaseController.PATH_SIGN_IN;
import static hu.psprog.leaflet.lcfa.web.controller.BaseController.PATH_SIGN_UP;

/**
 * Support logic for generating navigation bars on account pages.
 *
 * @author Peter Smith
 */
@Component
public class AccountNavigationBarSupport {

    private static final String SAME_PAGE_LINK = "''";

    private static final NavigationItem NAVIGATION_ITEM_PROFILE = NavigationItem.buildTranslatable(PATH_PROFILE, "section.profile.navigation.baseinfo");
    private static final NavigationItem NAVIGATION_ITEM_PASSWORD_CHANGE = NavigationItem.buildTranslatable(PATH_PROFILE_CHANGE_PASSWORD, "section.profile.navigation.pwchange");
    private static final NavigationItem NAVIGATION_ITEM_MY_COMMENTS = NavigationItem.buildTranslatable(PATH_PROFILE_MY_COMMENTS, "section.profile.navigation.comments");
    private static final NavigationItem NAVIGATION_ITEM_ACCOUNT_DELETION = NavigationItem.buildTranslatable(PATH_PROFILE_DELETE_ACCOUNT, "section.profile.navigation.delete");
    private static final NavigationItem NAVIGATION_ITEM_SIGN_IN = NavigationItem.buildTranslatable(PATH_SIGN_IN, "menu.sign_in");
    private static final NavigationItem NAVIGATION_ITEM_SIGN_UP = NavigationItem.buildTranslatable(PATH_SIGN_UP, "menu.sign_up");
    private static final NavigationItem NAVIGATION_ITEM_PASSWORD_RESET = NavigationItem.buildTranslatable(SAME_PAGE_LINK, "form.pwreset.title");

    private static final List<NavigationItem> NAVIGATION_ITEM_LIST_PROFILE = Collections.singletonList(NAVIGATION_ITEM_PROFILE);
    private static final List<NavigationItem> NAVIGATION_ITEM_LIST_PASSWORD_CHANGE = Arrays.asList(NAVIGATION_ITEM_PROFILE, NAVIGATION_ITEM_PASSWORD_CHANGE);
    private static final List<NavigationItem> NAVIGATION_ITEM_LIST_MY_COMMENTS = Arrays.asList(NAVIGATION_ITEM_PROFILE, NAVIGATION_ITEM_MY_COMMENTS);
    private static final List<NavigationItem> NAVIGATION_ITEM_LIST_ACCOUNT_DELETION = Arrays.asList(NAVIGATION_ITEM_PROFILE, NAVIGATION_ITEM_ACCOUNT_DELETION);
    private static final List<NavigationItem> NAVIGATION_ITEM_LIST_SIGN_IN = Collections.singletonList(NAVIGATION_ITEM_SIGN_IN);
    private static final List<NavigationItem> NAVIGATION_ITEM_LIST_SIGN_UP = Collections.singletonList(NAVIGATION_ITEM_SIGN_UP);
    private static final List<NavigationItem> NAVIGATION_ITEM_LIST_PASSWORD_RESET = Collections.singletonList(NAVIGATION_ITEM_PASSWORD_RESET);

    /**
     * Provides navigation bar items for Profile page.
     *
     * @return navigation bar items
     */
    public List<NavigationItem> profile() {
        return NAVIGATION_ITEM_LIST_PROFILE;
    }

    /**
     * Provides navigation bar items for Password change page.
     *
     * @return navigation bar items
     */
    public List<NavigationItem> passwordChange() {
        return NAVIGATION_ITEM_LIST_PASSWORD_CHANGE;
    }

    /**
     * Provides navigation bar items for My comments page.
     *
     * @return navigation bar items
     */
    public List<NavigationItem> myComments() {
        return NAVIGATION_ITEM_LIST_MY_COMMENTS;
    }

    /**
     * Provides navigation bar items for Account deletion page.
     *
     * @return navigation bar items
     */
    public List<NavigationItem> accountDeletion() {
        return NAVIGATION_ITEM_LIST_ACCOUNT_DELETION;
    }

    /**
     * Provides navigation bar items for Sign-in page.
     *
     * @return navigation bar items
     */
    public List<NavigationItem> signIn() {
        return NAVIGATION_ITEM_LIST_SIGN_IN;
    }

    /**
     * Provides navigation bar items for Sign-up page.
     *
     * @return navigation bar items
     */
    public List<NavigationItem> signUp() {
        return NAVIGATION_ITEM_LIST_SIGN_UP;
    }

    /**
     * Provides navigation bar items for Password reset page.
     *
     * @return navigation bar items
     */
    public List<NavigationItem> passwordReset() {
        return NAVIGATION_ITEM_LIST_PASSWORD_RESET;
    }
}
