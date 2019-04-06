package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.api.rest.request.user.UpdateProfileRequestModel;
import hu.psprog.leaflet.api.rest.response.user.ExtendedUserDataModel;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

/**
 * Unit tests for {@link UserProfileUpdateContentRequestAdapter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class UserProfileUpdateContentRequestAdapterTest {

    private static final Long USER_ID = 1L;
    private static final UpdateProfileRequestModel UPDATE_PROFILE_REQUEST_MODEL = new UpdateProfileRequestModel();
    private static final AccountRequestWrapper<UpdateProfileRequestModel> WRAPPED_UPDATE_PROFILE_REQUEST_MODEL =
            new AccountRequestWrapper<>(USER_ID, UPDATE_PROFILE_REQUEST_MODEL);
    private static final ExtendedUserDataModel EXTENDED_USER_DATA_MODEL = ExtendedUserDataModel.getExtendedBuilder().build();

    @Mock
    private UserBridgeService userBridgeService;

    @InjectMocks
    private UserProfileUpdateContentRequestAdapter adapter;

    @Test
    public void shouldGetContentReturnWithSuccess() throws CommunicationFailureException {

        // given
        given(userBridgeService.updateProfile(USER_ID, UPDATE_PROFILE_REQUEST_MODEL)).willReturn(EXTENDED_USER_DATA_MODEL);

        // when
        Optional<ExtendedUserDataModel> result = adapter.getContent(WRAPPED_UPDATE_PROFILE_REQUEST_MODEL);

        // then
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(EXTENDED_USER_DATA_MODEL));
    }

    @Test(expected = UserSessionInvalidationRequiredException.class)
    public void shouldGetContentReturnThrowUserSessionInvalidationRequiredException() throws CommunicationFailureException {

        // given
        doThrow(UnauthorizedAccessException.class).when(userBridgeService).updateProfile(USER_ID, UPDATE_PROFILE_REQUEST_MODEL);

        // when
        adapter.getContent(WRAPPED_UPDATE_PROFILE_REQUEST_MODEL);

        // then
        // exception expected
    }

    @Test
    public void shouldGetContentReturnWithFailureInCaseOfBridgeFailure() throws CommunicationFailureException {

        // given
        doThrow(CommunicationFailureException.class).when(userBridgeService).updateProfile(USER_ID, UPDATE_PROFILE_REQUEST_MODEL);

        // when
        Optional<ExtendedUserDataModel> result = adapter.getContent(WRAPPED_UPDATE_PROFILE_REQUEST_MODEL);

        // then
        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void shouldGetIdentifierReturnProperValue() {

        // when
        ContentRequestAdapterIdentifier result = adapter.getIdentifier();

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(ContentRequestAdapterIdentifier.PROFILE_UPDATE));
    }
}
