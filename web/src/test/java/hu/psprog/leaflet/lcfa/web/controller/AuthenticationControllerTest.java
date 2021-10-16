package hu.psprog.leaflet.lcfa.web.controller;

import hu.psprog.leaflet.jwt.auth.support.domain.JWTTokenAuthentication;
import hu.psprog.leaflet.lcfa.core.domain.request.PasswordReclaimRequestModel;
import hu.psprog.leaflet.lcfa.core.domain.request.PasswordResetConfirmationRequestModel;
import hu.psprog.leaflet.lcfa.core.domain.request.SignUpRequestModel;
import hu.psprog.leaflet.lcfa.core.domain.result.PasswordResetResult;
import hu.psprog.leaflet.lcfa.core.domain.result.SignUpResult;
import hu.psprog.leaflet.lcfa.core.facade.AuthenticationFacade;
import hu.psprog.leaflet.lcfa.web.auth.mock.WithMockedJWTUser;
import hu.psprog.leaflet.lcfa.web.model.FlashMessageKey;
import hu.psprog.leaflet.lcfa.web.model.ModelField;
import hu.psprog.leaflet.lcfa.web.model.NavigationItem;
import hu.psprog.leaflet.lcfa.web.ui.support.navigation.AccountNavigationBarSupport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static hu.psprog.leaflet.lcfa.web.controller.BaseController.PATH_HOME;
import static hu.psprog.leaflet.lcfa.web.controller.BaseController.PATH_SIGN_IN;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * Unit tests for {@link AuthenticationController}.
 *
 * @author Peter Smith
 */
@Extensions({
        @ExtendWith(MockitoExtension.class),
        @ExtendWith(SpringExtension.class)
})
public class AuthenticationControllerTest extends AbstractControllerTest {

    private static final List<NavigationItem> NAVIGATION_ITEM_LIST = Collections.singletonList(NavigationItem.build("link", "title"));

    private static final String RESET_TOKEN = "reset-token";
    private static final PasswordReclaimRequestModel PASSWORD_RECLAIM_REQUEST_MODEL = new PasswordReclaimRequestModel();
    private static final SignUpRequestModel SIGN_UP_REQUEST_MODEL = new SignUpRequestModel();
    private static final PasswordResetConfirmationRequestModel PASSWORD_RESET_CONFIRMATION_REQUEST_MODEL = new PasswordResetConfirmationRequestModel();

    private static final String VIEW_GROUP_USERS = "users";
    private static final String VIEW_SIGN_IN = "sign_in";
    private static final String VIEW_SIGN_UP = "sign_up";
    private static final String VIEW_PASSWORD_RESET = "pw_reset";

    static {
        SIGN_UP_REQUEST_MODEL.setEmail("email");
        PASSWORD_RECLAIM_REQUEST_MODEL.setEmail("email");
        PASSWORD_RESET_CONFIRMATION_REQUEST_MODEL.setPassword("password");
    }

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private AccountNavigationBarSupport accountNavigationBarSupport;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Test
    public void shouldRenderSignInForm() {

        // given
        given(accountNavigationBarSupport.signIn()).willReturn(NAVIGATION_ITEM_LIST);

        // when
        authenticationController.renderSignInForm(PASSWORD_RECLAIM_REQUEST_MODEL);

        // then
        verifyViewCreated(VIEW_SIGN_IN);
        verifyFieldSet(ModelField.NAVIGATION, NAVIGATION_ITEM_LIST);
    }

    @Test
    public void shouldRenderSignUpForm() {

        // given
        given(accountNavigationBarSupport.signUp()).willReturn(NAVIGATION_ITEM_LIST);

        // when
        authenticationController.renderSignUpForm(SIGN_UP_REQUEST_MODEL);

        // then
        verifyViewCreated(VIEW_SIGN_UP);
        verifyFieldSet(ModelField.VALIDATED_MODEL, SIGN_UP_REQUEST_MODEL);
        verifyFieldSet(ModelField.NAVIGATION, NAVIGATION_ITEM_LIST);
    }

    @Test
    public void shouldProcessSignUpRequestWithSuccess() {

        // given
        given(authenticationFacade.signUp(SIGN_UP_REQUEST_MODEL)).willReturn(SignUpResult.SUCCESS);

        // when
        authenticationController.processSignUpRequest(SIGN_UP_REQUEST_MODEL, bindingResult, redirectAttributes);

        // then
        verifyRedirectionCreated(BaseController.PATH_SIGN_IN);
        verifyFlashMessageSet(FlashMessageKey.SUCCESSFUL_SIGN_UP);
    }

    @Test
    public void shouldProcessSignUpRequestWithAlreadyUsedAddress() {

        // given
        given(authenticationFacade.signUp(SIGN_UP_REQUEST_MODEL)).willReturn(SignUpResult.ADDRESS_IN_USE);

        // when
        authenticationController.processSignUpRequest(SIGN_UP_REQUEST_MODEL, bindingResult, redirectAttributes);

        // then
        verifyRedirectionCreated(BaseController.PATH_SIGN_UP);
        verifyFlashMessageSet(FlashMessageKey.FAILED_SIGN_UP_ADDRESS_ALREADY_IN_USE);
    }

    @Test
    public void shouldProcessSignUpRequestWithGeneralFailure() {

        // given
        given(authenticationFacade.signUp(SIGN_UP_REQUEST_MODEL)).willReturn(SignUpResult.FAILURE);

        // when
        authenticationController.processSignUpRequest(SIGN_UP_REQUEST_MODEL, bindingResult, redirectAttributes);

        // then
        verifyRedirectionCreated(BaseController.PATH_SIGN_UP);
        verifyFlashMessageSet(FlashMessageKey.FAILED_SIGN_UP_UNKNOWN_ERROR);
    }

    @Test
    public void shouldProcessSignUpRequestWithValidationError() {

        // given
        given(bindingResult.hasErrors()).willReturn(true);
        given(accountNavigationBarSupport.signUp()).willReturn(NAVIGATION_ITEM_LIST);

        // when
        authenticationController.processSignUpRequest(SIGN_UP_REQUEST_MODEL, bindingResult, redirectAttributes);

        // then
        verifyViewCreated(VIEW_SIGN_UP);
        verifyFieldSet(ModelField.VALIDATED_MODEL, SIGN_UP_REQUEST_MODEL);
        verifyFieldSet(ModelField.NAVIGATION, NAVIGATION_ITEM_LIST);
        verifyNoInteractions(authenticationFacade);
    }

    @Test
    public void shouldProcessPasswordResetRequestWithSuccess() {

        // given
        given(authenticationFacade.requestPasswordReset(PASSWORD_RECLAIM_REQUEST_MODEL)).willReturn(PasswordResetResult.DEMAND_PROCESSED);

        // when
        authenticationController.processPasswordResetRequest(PASSWORD_RECLAIM_REQUEST_MODEL, bindingResult, redirectAttributes);

        // then
        verifyRedirectionCreated(PATH_HOME);
        verifyFlashMessageSet(FlashMessageKey.SUCCESSFUL_PASSWORD_RESET_DEMAND);
    }

    @Test
    public void shouldProcessPasswordResetRequestWithFailure() {

        // given
        given(authenticationFacade.requestPasswordReset(PASSWORD_RECLAIM_REQUEST_MODEL)).willReturn(PasswordResetResult.DEMAND_FAILED);

        // when
        authenticationController.processPasswordResetRequest(PASSWORD_RECLAIM_REQUEST_MODEL, bindingResult, redirectAttributes);

        // then
        verifyRedirectionCreated(PATH_SIGN_IN);
        verifyFlashMessageSet(FlashMessageKey.FAILED_PASSWORD_RESET_DEMAND);
    }

    @Test
    public void shouldProcessPasswordResetRequestWithValidationError() {

        // given
        given(accountNavigationBarSupport.signIn()).willReturn(NAVIGATION_ITEM_LIST);
        given(bindingResult.hasErrors()).willReturn(true);

        // when
        authenticationController.processPasswordResetRequest(PASSWORD_RECLAIM_REQUEST_MODEL, bindingResult, redirectAttributes);

        // then
        verifyViewCreated(VIEW_SIGN_IN);
        verifyFieldSet(ModelField.NAVIGATION, NAVIGATION_ITEM_LIST);
    }

    @Test
    public void shouldRenderPasswordResetConfirmationForm() {

        // given
        given(accountNavigationBarSupport.passwordReset()).willReturn(NAVIGATION_ITEM_LIST);

        // when
        authenticationController.renderPasswordResetConfirmationForm(RESET_TOKEN, PASSWORD_RESET_CONFIRMATION_REQUEST_MODEL);

        // then
        verifyViewCreated(VIEW_PASSWORD_RESET);
        verifyFieldSet(ModelField.NAVIGATION, NAVIGATION_ITEM_LIST);
    }

    @Test
    @WithMockedJWTUser
    public void shouldProcessPasswordResetConfirmationWithSuccess() {

        // given
        assertSecurityContext();
        given(authenticationFacade.confirmPasswordReset(PASSWORD_RESET_CONFIRMATION_REQUEST_MODEL, RESET_TOKEN)).willReturn(PasswordResetResult.CONFIRMATION_PROCESSED);

        // when
        authenticationController.processPasswordResetConfirmationForm(RESET_TOKEN, PASSWORD_RESET_CONFIRMATION_REQUEST_MODEL, bindingResult, redirectAttributes);

        // then
        verifyRedirectionCreated(PATH_SIGN_IN);
        verifyFlashMessageSet(FlashMessageKey.SUCCESSFUL_PASSWORD_RESET_CONFIRMATION);
        verifyEmptySecurityContext();
    }

    @Test
    @WithMockedJWTUser
    public void shouldProcessPasswordResetConfirmationWithFailure() {

        // given
        assertSecurityContext();
        given(authenticationFacade.confirmPasswordReset(PASSWORD_RESET_CONFIRMATION_REQUEST_MODEL, RESET_TOKEN)).willReturn(PasswordResetResult.CONFIRMATION_FAILED);

        // when
        authenticationController.processPasswordResetConfirmationForm(RESET_TOKEN, PASSWORD_RESET_CONFIRMATION_REQUEST_MODEL, bindingResult, redirectAttributes);

        // then
        verifyRedirectionCreated(PATH_SIGN_IN);
        verifyFlashMessageSet(FlashMessageKey.FAILED_PASSWORD_RESET_CONFIRMATION);
        verifyEmptySecurityContext();
    }

    @Test
    public void shouldProcessPasswordResetConfirmationWithValidationError() {

        // given
        given(bindingResult.hasErrors()).willReturn(true);
        given(accountNavigationBarSupport.passwordReset()).willReturn(NAVIGATION_ITEM_LIST);

        // when
        authenticationController.processPasswordResetConfirmationForm(RESET_TOKEN, PASSWORD_RESET_CONFIRMATION_REQUEST_MODEL, bindingResult, redirectAttributes);

        // then
        verifyViewCreated(VIEW_PASSWORD_RESET);
        verifyFieldSet(ModelField.NAVIGATION, NAVIGATION_ITEM_LIST);
    }

    @Override
    String controllerViewGroup() {
        return VIEW_GROUP_USERS;
    }

    private void assertSecurityContext() {
        assertThat(Objects.nonNull(SecurityContextHolder.getContext().getAuthentication())
                && SecurityContextHolder.getContext().getAuthentication() instanceof JWTTokenAuthentication, is(true));
    }

    private void verifyEmptySecurityContext() {
        assertThat(Objects.isNull(SecurityContextHolder.getContext().getAuthentication()), is(true));
    }
}
