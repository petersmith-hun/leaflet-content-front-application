package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.api.rest.request.user.PasswordChangeRequestModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.exception.UnauthorizedAccessException;
import hu.psprog.leaflet.bridge.service.UserBridgeService;
import hu.psprog.leaflet.lcfa.core.domain.request.AccountRequestWrapper;
import hu.psprog.leaflet.lcfa.core.exception.UserSessionInvalidationRequiredException;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link UserPasswordChangeContentRequestAdapter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class UserPasswordChangeContentRequestAdapterTest {

    private static final Long USER_ID = 1L;
    private static final PasswordChangeRequestModel PASSWORD_CHANGE_REQUEST_MODEL = new PasswordChangeRequestModel();
    private static final AccountRequestWrapper<PasswordChangeRequestModel> WRAPPED_PASSWORD_CHANGE_REQUEST_MODEL =
            new AccountRequestWrapper<>(USER_ID, PASSWORD_CHANGE_REQUEST_MODEL);

    @Mock
    private UserBridgeService userBridgeService;

    @InjectMocks
    private UserPasswordChangeContentRequestAdapter adapter;

    @Test
    public void shouldGetContentReturnWithSuccess() throws CommunicationFailureException {

        // when
        Optional<Boolean> result = adapter.getContent(WRAPPED_PASSWORD_CHANGE_REQUEST_MODEL);

        // then
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(true));
        verify(userBridgeService).updatePassword(USER_ID, PASSWORD_CHANGE_REQUEST_MODEL);
    }

    @Test(expected = UserSessionInvalidationRequiredException.class)
    public void shouldGetContentReturnThrowUserSessionInvalidationRequiredException() throws CommunicationFailureException {

        // given
        doThrow(UnauthorizedAccessException.class).when(userBridgeService).updatePassword(USER_ID, PASSWORD_CHANGE_REQUEST_MODEL);

        // when
        adapter.getContent(WRAPPED_PASSWORD_CHANGE_REQUEST_MODEL);

        // then
        // exception expected
    }

    @Test
    public void shouldGetContentReturnWithFailureInCaseOfBridgeFailure() throws CommunicationFailureException {

        // given
        doThrow(CommunicationFailureException.class).when(userBridgeService).updatePassword(USER_ID, PASSWORD_CHANGE_REQUEST_MODEL);

        // when
        Optional<Boolean> result = adapter.getContent(WRAPPED_PASSWORD_CHANGE_REQUEST_MODEL);

        // then
        assertThat(result.isPresent(), is(false));
        verify(userBridgeService).updatePassword(USER_ID, PASSWORD_CHANGE_REQUEST_MODEL);
    }

    @Test
    public void shouldGetIdentifierReturnProperValue() {

        // when
        ContentRequestAdapterIdentifier result = adapter.getIdentifier();

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(ContentRequestAdapterIdentifier.PROFILE_PASSWORD_CHANGE));
    }
}
