package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.api.rest.request.user.UserInitializeRequestModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.exception.ConflictingRequestException;
import hu.psprog.leaflet.bridge.service.UserBridgeService;
import hu.psprog.leaflet.lcfa.core.converter.SignUpRequestConverter;
import hu.psprog.leaflet.lcfa.core.domain.request.SignUpRequestModel;
import hu.psprog.leaflet.lcfa.core.domain.result.SignUpResult;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link SignUpRequestContentRequestAdapter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class SignUpRequestContentRequestAdapterTest {

    private static final SignUpRequestModel SIGN_UP_REQUEST_MODEL = new SignUpRequestModel();
    private static final UserInitializeRequestModel USER_INITIALIZE_REQUEST_MODEL = new UserInitializeRequestModel();
    private static final String RECAPTCHA_TOKEN = "recaptcha-token";

    static {
        SIGN_UP_REQUEST_MODEL.setRecaptchaToken(RECAPTCHA_TOKEN);
    }

    @Mock
    private UserBridgeService userBridgeService;

    @Mock
    private SignUpRequestConverter signUpRequestConverter;

    @InjectMocks
    private SignUpRequestContentRequestAdapter adapter;

    @Test
    public void shouldGetContentReturnWithSuccessStatus() throws CommunicationFailureException {

        // given
        given(signUpRequestConverter.convert(SIGN_UP_REQUEST_MODEL)).willReturn(USER_INITIALIZE_REQUEST_MODEL);

        // when
        Optional<SignUpResult> result = adapter.getContent(SIGN_UP_REQUEST_MODEL);

        // then
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), equalTo(SignUpResult.SUCCESS));
        verify(userBridgeService).signUp(USER_INITIALIZE_REQUEST_MODEL, RECAPTCHA_TOKEN);
    }

    @Test
    public void shouldGetContentReturnWithAddressInUseStatus() throws CommunicationFailureException {

        // given
        given(signUpRequestConverter.convert(SIGN_UP_REQUEST_MODEL)).willReturn(USER_INITIALIZE_REQUEST_MODEL);
        doThrow(ConflictingRequestException.class).when(userBridgeService).signUp(USER_INITIALIZE_REQUEST_MODEL, RECAPTCHA_TOKEN);

        // when
        Optional<SignUpResult> result = adapter.getContent(SIGN_UP_REQUEST_MODEL);

        // then
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), equalTo(SignUpResult.ADDRESS_IN_USE));
        verify(userBridgeService).signUp(USER_INITIALIZE_REQUEST_MODEL, RECAPTCHA_TOKEN);
    }

    @Test
    public void shouldGetContentReturnWithFailureStatus() throws CommunicationFailureException {

        // given
        given(signUpRequestConverter.convert(SIGN_UP_REQUEST_MODEL)).willReturn(USER_INITIALIZE_REQUEST_MODEL);
        doThrow(CommunicationFailureException.class).when(userBridgeService).signUp(USER_INITIALIZE_REQUEST_MODEL, RECAPTCHA_TOKEN);

        // when
        Optional<SignUpResult> result = adapter.getContent(SIGN_UP_REQUEST_MODEL);

        // then
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), equalTo(SignUpResult.FAILURE));
        verify(userBridgeService).signUp(USER_INITIALIZE_REQUEST_MODEL, RECAPTCHA_TOKEN);
    }

    @Test
    public void shouldGetIdentifierReturnProperValue() {

        // when
        ContentRequestAdapterIdentifier result = adapter.getIdentifier();

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(ContentRequestAdapterIdentifier.SIGN_UP));
    }
}
