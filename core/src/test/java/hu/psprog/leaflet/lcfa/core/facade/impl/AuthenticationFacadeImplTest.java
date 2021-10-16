package hu.psprog.leaflet.lcfa.core.facade.impl;

import hu.psprog.leaflet.jwt.auth.support.service.impl.utility.AuthenticationUtility;
import hu.psprog.leaflet.lcfa.core.domain.request.PasswordReclaimRequestModel;
import hu.psprog.leaflet.lcfa.core.domain.request.PasswordResetConfirmationRequestModel;
import hu.psprog.leaflet.lcfa.core.domain.request.SignUpRequestModel;
import hu.psprog.leaflet.lcfa.core.domain.result.PasswordResetResult;
import hu.psprog.leaflet.lcfa.core.domain.result.SignUpResult;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapter;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link AuthenticationFacadeImpl}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class AuthenticationFacadeImplTest {

    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String RECLAIM_TOKEN = "reclaim-token";

    private static final SignUpRequestModel SIGN_UP_REQUEST_MODEL = new SignUpRequestModel();
    private static final PasswordReclaimRequestModel PASSWORD_RECLAIM_REQUEST_MODEL = new PasswordReclaimRequestModel();
    private static final PasswordResetConfirmationRequestModel PASSWORD_RESET_CONFIRMATION_REQUEST_MODEL = new PasswordResetConfirmationRequestModel();

    static {
        SIGN_UP_REQUEST_MODEL.setEmail(EMAIL);
        PASSWORD_RECLAIM_REQUEST_MODEL.setEmail(EMAIL);
        PASSWORD_RESET_CONFIRMATION_REQUEST_MODEL.setPassword(PASSWORD);
    }

    @Mock
    private ContentRequestAdapterRegistry contentRequestAdapterRegistry;

    @Mock
    private AuthenticationUtility authenticationUtility;

    @Mock
    private ContentRequestAdapter<SignUpResult, SignUpRequestModel> signUpContentRequestAdapter;

    @Mock
    private ContentRequestAdapter<PasswordResetResult, PasswordReclaimRequestModel> passwordResetRequestContentRequestAdapter;

    @Mock
    private ContentRequestAdapter<PasswordResetResult, PasswordResetConfirmationRequestModel> passwordResetConfirmationContentRequestAdapter;

    @InjectMocks
    private AuthenticationFacadeImpl authenticationFacade;

    @Test
    public void shouldSignUpWithSuccess() {

        // given
        given(contentRequestAdapterRegistry.<SignUpResult, SignUpRequestModel>getContentRequestAdapter(ContentRequestAdapterIdentifier.SIGN_UP))
                .willReturn(signUpContentRequestAdapter);
        given(signUpContentRequestAdapter.getContent(SIGN_UP_REQUEST_MODEL)).willReturn(Optional.of(SignUpResult.SUCCESS));

        // when
        SignUpResult result = authenticationFacade.signUp(SIGN_UP_REQUEST_MODEL);

        // then
        assertThat(result, equalTo(SignUpResult.SUCCESS));
    }

    @Test
    public void shouldSignUpWithFailureForMissingData() {

        // given
        given(contentRequestAdapterRegistry.<SignUpResult, SignUpRequestModel>getContentRequestAdapter(ContentRequestAdapterIdentifier.SIGN_UP))
                .willReturn(signUpContentRequestAdapter);
        given(signUpContentRequestAdapter.getContent(SIGN_UP_REQUEST_MODEL)).willReturn(Optional.empty());

        // when
        SignUpResult result = authenticationFacade.signUp(SIGN_UP_REQUEST_MODEL);

        // then
        assertThat(result, equalTo(SignUpResult.FAILURE));
    }

    @Test
    public void shouldRequestPasswordResetWithSuccess() {

        // given
        given(contentRequestAdapterRegistry.<PasswordResetResult, PasswordReclaimRequestModel>getContentRequestAdapter(ContentRequestAdapterIdentifier.PASSWORD_RESET_REQUEST))
                .willReturn(passwordResetRequestContentRequestAdapter);
        given(passwordResetRequestContentRequestAdapter.getContent(PASSWORD_RECLAIM_REQUEST_MODEL)).willReturn(Optional.of(PasswordResetResult.DEMAND_PROCESSED));

        // when
        PasswordResetResult result = authenticationFacade.requestPasswordReset(PASSWORD_RECLAIM_REQUEST_MODEL);

        // then
        assertThat(result, equalTo(PasswordResetResult.DEMAND_PROCESSED));
    }

    @Test
    public void shouldRequestPasswordResetWithFailureForMissingData() {

        // given
        given(contentRequestAdapterRegistry.<PasswordResetResult, PasswordReclaimRequestModel>getContentRequestAdapter(ContentRequestAdapterIdentifier.PASSWORD_RESET_REQUEST))
                .willReturn(passwordResetRequestContentRequestAdapter);
        given(passwordResetRequestContentRequestAdapter.getContent(PASSWORD_RECLAIM_REQUEST_MODEL)).willReturn(Optional.empty());

        // when
        PasswordResetResult result = authenticationFacade.requestPasswordReset(PASSWORD_RECLAIM_REQUEST_MODEL);

        // then
        assertThat(result, equalTo(PasswordResetResult.DEMAND_FAILED));
    }

    @Test
    public void shouldConfirmPasswordResetWithSuccess() {

        // given
        given(contentRequestAdapterRegistry.<PasswordResetResult, PasswordResetConfirmationRequestModel>getContentRequestAdapter(ContentRequestAdapterIdentifier.PASSWORD_RESET_CONFIRMATION))
                .willReturn(passwordResetConfirmationContentRequestAdapter);
        given(passwordResetConfirmationContentRequestAdapter.getContent(PASSWORD_RESET_CONFIRMATION_REQUEST_MODEL)).willReturn(Optional.of(PasswordResetResult.CONFIRMATION_PROCESSED));

        // when
        PasswordResetResult result = authenticationFacade.confirmPasswordReset(PASSWORD_RESET_CONFIRMATION_REQUEST_MODEL, RECLAIM_TOKEN);

        // then
        assertThat(result, equalTo(PasswordResetResult.CONFIRMATION_PROCESSED));
        verify(authenticationUtility).createAndStoreTemporal(RECLAIM_TOKEN);
    }

    @Test
    public void shouldConfirmPasswordResetWithFailureForMissingData() {

        // given
        given(contentRequestAdapterRegistry.<PasswordResetResult, PasswordResetConfirmationRequestModel>getContentRequestAdapter(ContentRequestAdapterIdentifier.PASSWORD_RESET_CONFIRMATION))
                .willReturn(passwordResetConfirmationContentRequestAdapter);
        given(passwordResetConfirmationContentRequestAdapter.getContent(PASSWORD_RESET_CONFIRMATION_REQUEST_MODEL)).willReturn(Optional.empty());

        // when
        PasswordResetResult result = authenticationFacade.confirmPasswordReset(PASSWORD_RESET_CONFIRMATION_REQUEST_MODEL, RECLAIM_TOKEN);

        // then
        assertThat(result, equalTo(PasswordResetResult.CONFIRMATION_FAILED));
        verify(authenticationUtility).createAndStoreTemporal(RECLAIM_TOKEN);
    }
}
