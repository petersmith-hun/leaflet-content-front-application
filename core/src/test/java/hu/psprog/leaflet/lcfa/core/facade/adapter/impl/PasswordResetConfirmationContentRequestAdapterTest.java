package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.exception.UnauthorizedAccessException;
import hu.psprog.leaflet.bridge.service.UserBridgeService;
import hu.psprog.leaflet.lcfa.core.domain.request.PasswordResetConfirmationRequestModel;
import hu.psprog.leaflet.lcfa.core.domain.result.PasswordResetResult;
import hu.psprog.leaflet.lcfa.core.exception.UserSessionInvalidationRequiredException;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link PasswordResetConfirmationContentRequestAdapter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class PasswordResetConfirmationContentRequestAdapterTest {

    private static final PasswordResetConfirmationRequestModel PASSWORD_RESET_CONFIRMATION_REQUEST_MODEL = new PasswordResetConfirmationRequestModel();
    private static final String RECAPTCHA_TOKEN = "recaptcha-token";

    static {
        PASSWORD_RESET_CONFIRMATION_REQUEST_MODEL.setRecaptchaToken(RECAPTCHA_TOKEN);
    }

    @Mock
    private UserBridgeService userBridgeService;

    @InjectMocks
    private PasswordResetConfirmationContentRequestAdapter adapter;

    @Test
    public void shouldGetContentReturnWithConfirmationProcessedStatus() throws CommunicationFailureException {

        // when
        Optional<PasswordResetResult> result = adapter.getContent(PASSWORD_RESET_CONFIRMATION_REQUEST_MODEL);

        // then
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), equalTo(PasswordResetResult.CONFIRMATION_PROCESSED));
        verify(userBridgeService).confirmPasswordReset(PASSWORD_RESET_CONFIRMATION_REQUEST_MODEL, RECAPTCHA_TOKEN);
    }

    @Test
    public void shouldGetContentReturnWithConfirmationFailedStatusInCaseOfBridgeFailure() throws CommunicationFailureException {

        // given
        doThrow(CommunicationFailureException.class).when(userBridgeService).confirmPasswordReset(PASSWORD_RESET_CONFIRMATION_REQUEST_MODEL, RECAPTCHA_TOKEN);

        // when
        Optional<PasswordResetResult> result = adapter.getContent(PASSWORD_RESET_CONFIRMATION_REQUEST_MODEL);

        // then
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), equalTo(PasswordResetResult.CONFIRMATION_FAILED));
        verify(userBridgeService).confirmPasswordReset(PASSWORD_RESET_CONFIRMATION_REQUEST_MODEL, RECAPTCHA_TOKEN);
    }

    @Test
    public void shouldGetContentReturnThrowUserSessionInvalidationRequiredException() throws CommunicationFailureException {

        // given
        doThrow(UnauthorizedAccessException.class).when(userBridgeService).confirmPasswordReset(PASSWORD_RESET_CONFIRMATION_REQUEST_MODEL, RECAPTCHA_TOKEN);

        // when
        Assertions.assertThrows(UserSessionInvalidationRequiredException.class, () ->adapter.getContent(PASSWORD_RESET_CONFIRMATION_REQUEST_MODEL));

        // then
        // exception expected
    }

    @Test
    public void shouldGetIdentifierReturnProperValue() {

        // when
        ContentRequestAdapterIdentifier result = adapter.getIdentifier();

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(ContentRequestAdapterIdentifier.PASSWORD_RESET_CONFIRMATION));
    }
}
