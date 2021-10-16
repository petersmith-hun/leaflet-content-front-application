package hu.psprog.leaflet.lcfa.web.thymeleaf.support.security;

import hu.psprog.leaflet.lcfa.core.domain.common.FrontEndRouteAuthRequirement;
import hu.psprog.leaflet.lcfa.core.domain.common.MenuItem;
import hu.psprog.leaflet.lcfa.web.auth.mock.WithMockedJWTUser;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link FrontEndRouteAuthRequirementEvaluator}.
 *
 * @author Peter Smith
 */
@Extensions({
        @ExtendWith(MockitoExtension.class),
        @ExtendWith(SpringExtension.class)
})
public class FrontEndRouteAuthRequirementEvaluatorTest {

    private static final MenuItem MENU_ITEM_SHOW_ALWAYS = MenuItem.builder().authRequirement(FrontEndRouteAuthRequirement.SHOW_ALWAYS).build();
    private static final MenuItem MENU_ITEM_AUTHENTICATED = MenuItem.builder().authRequirement(FrontEndRouteAuthRequirement.AUTHENTICATED).build();
    private static final MenuItem MENU_ITEM_ANONYMOUS = MenuItem.builder().authRequirement(FrontEndRouteAuthRequirement.ANONYMOUS).build();

    @InjectMocks
    private FrontEndRouteAuthRequirementEvaluator frontEndRouteAuthRequirementEvaluator;

    @ParameterizedTest
    @WithMockedJWTUser
    @MethodSource("authenticatedMenuDataProvider")
    public void shouldCanDisplayEvaluateMenuItemForAuthenticatedCases(MenuItem menuItem, boolean shouldDisplay) {

        // when
        boolean result = frontEndRouteAuthRequirementEvaluator.canDisplay(menuItem);

        // then
        assertThat(result, is(shouldDisplay));
    }

    @ParameterizedTest
    @WithMockUser
    @MethodSource("nonJWTMenuDataProvider")
    public void shouldCanDisplayEvaluateMenuItemForNonJWTAuthenticatedCases(MenuItem menuItem, boolean shouldDisplay) {

        // when
        boolean result = frontEndRouteAuthRequirementEvaluator.canDisplay(menuItem);

        // then
        assertThat(result, is(shouldDisplay));
    }

    @ParameterizedTest
    @WithAnonymousUser
    @MethodSource("anonymousMenuDataProvider")
    public void shouldCanDisplayEvaluateMenuItemForAnonymousCases(MenuItem menuItem, boolean shouldDisplay) {

        // when
        boolean result = frontEndRouteAuthRequirementEvaluator.canDisplay(menuItem);

        // then
        assertThat(result, is(shouldDisplay));
    }

    private static Stream<Arguments> authenticatedMenuDataProvider() {

        return Stream.of(
                Arguments.of(MENU_ITEM_SHOW_ALWAYS, true),
                Arguments.of(MENU_ITEM_AUTHENTICATED, true),
                Arguments.of(MENU_ITEM_ANONYMOUS, false)
        );
    }

    private static Stream<Arguments> anonymousMenuDataProvider() {

        return Stream.of(
                Arguments.of(MENU_ITEM_SHOW_ALWAYS, true),
                Arguments.of(MENU_ITEM_AUTHENTICATED, false),
                Arguments.of(MENU_ITEM_ANONYMOUS, true)
        );
    }

    private static Stream<Arguments> nonJWTMenuDataProvider() {

        return Stream.of(
                Arguments.of(MENU_ITEM_SHOW_ALWAYS, true),
                Arguments.of(MENU_ITEM_AUTHENTICATED, false),
                Arguments.of(MENU_ITEM_ANONYMOUS, false)
        );
    }
}
