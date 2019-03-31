package hu.psprog.leaflet.lcfa.web.thymeleaf.support.security;

import hu.psprog.leaflet.lcfa.core.domain.common.FrontEndRouteAuthRequirement;
import hu.psprog.leaflet.lcfa.core.domain.common.MenuItem;
import hu.psprog.leaflet.lcfa.web.auth.mock.WithMockedJWTUser;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link FrontEndRouteAuthRequirementEvaluator}.
 *
 * @author Peter Smith
 */
@RunWith(JUnitParamsRunner.class)
public class FrontEndRouteAuthRequirementEvaluatorTest {

    private static final MenuItem MENU_ITEM_SHOW_ALWAYS = MenuItem.builder().authRequirement(FrontEndRouteAuthRequirement.SHOW_ALWAYS).build();
    private static final MenuItem MENU_ITEM_AUTHENTICATED = MenuItem.builder().authRequirement(FrontEndRouteAuthRequirement.AUTHENTICATED).build();
    private static final MenuItem MENU_ITEM_ANONYMOUS = MenuItem.builder().authRequirement(FrontEndRouteAuthRequirement.ANONYMOUS).build();

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @InjectMocks
    private FrontEndRouteAuthRequirementEvaluator frontEndRouteAuthRequirementEvaluator;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @WithMockedJWTUser
    @Parameters(source = MenuItemProvider.class, method = "authenticated")
    public void shouldCanDisplayEvaluateMenuItemForAuthenticatedCases(MenuItem menuItem, boolean shouldDisplay) {

        // when
        boolean result = frontEndRouteAuthRequirementEvaluator.canDisplay(menuItem);

        // then
        assertThat(result, is(shouldDisplay));
    }

    @Test
    @WithMockUser
    @Parameters(source = MenuItemProvider.class, method = "nonJWT")
    public void shouldCanDisplayEvaluateMenuItemForNonJWTAuthenticatedCases(MenuItem menuItem, boolean shouldDisplay) {

        // when
        boolean result = frontEndRouteAuthRequirementEvaluator.canDisplay(menuItem);

        // then
        assertThat(result, is(shouldDisplay));
    }

    @Test
    @WithAnonymousUser
    @Parameters(source = MenuItemProvider.class, method = "anonymous")
    public void shouldCanDisplayEvaluateMenuItemForAnonymousCases(MenuItem menuItem, boolean shouldDisplay) {

        // when
        boolean result = frontEndRouteAuthRequirementEvaluator.canDisplay(menuItem);

        // then
        assertThat(result, is(shouldDisplay));
    }

    public static class MenuItemProvider {

        public static Object[] authenticated() {

            return new Object[] {
                    new Object[] {MENU_ITEM_SHOW_ALWAYS, true},
                    new Object[] {MENU_ITEM_AUTHENTICATED, true},
                    new Object[] {MENU_ITEM_ANONYMOUS, false},
            };
        }

        public static Object[] anonymous() {

            return new Object[] {
                    new Object[] {MENU_ITEM_SHOW_ALWAYS, true},
                    new Object[] {MENU_ITEM_AUTHENTICATED, false},
                    new Object[] {MENU_ITEM_ANONYMOUS, true},
            };
        }

        public static Object[] nonJWT() {

            return new Object[] {
                    new Object[] {MENU_ITEM_SHOW_ALWAYS, true},
                    new Object[] {MENU_ITEM_AUTHENTICATED, false},
                    new Object[] {MENU_ITEM_ANONYMOUS, false},
            };
        }
    }
}
