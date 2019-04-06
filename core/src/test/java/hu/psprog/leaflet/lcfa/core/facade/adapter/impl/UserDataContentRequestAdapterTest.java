package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.api.rest.response.user.ExtendedUserDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.exception.UnauthorizedAccessException;
import hu.psprog.leaflet.bridge.service.UserBridgeService;
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
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link UserDataContentRequestAdapter}.
 * 
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class UserDataContentRequestAdapterTest {

    private static final Long USER_ID = 1L;
    private static final ExtendedUserDataModel EXTENDED_USER_DATA_MODEL = ExtendedUserDataModel.getExtendedBuilder().build();

    @Mock
    private UserBridgeService userBridgeService;
    
    @InjectMocks
    private UserDataContentRequestAdapter adapter;

    @Test
    public void shouldGetContentReturnWithSuccess() throws CommunicationFailureException {

        // given
        given(userBridgeService.getUserByID(USER_ID)).willReturn(EXTENDED_USER_DATA_MODEL);

        // when
        Optional<ExtendedUserDataModel> result = adapter.getContent(USER_ID);

        // then
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), equalTo(EXTENDED_USER_DATA_MODEL));
    }

    @Test(expected = UserSessionInvalidationRequiredException.class)
    public void shouldGetContentReturnThrowUserSessionInvalidationRequiredException() throws CommunicationFailureException {

        // given
        doThrow(UnauthorizedAccessException.class).when(userBridgeService).getUserByID(USER_ID);

        // when
        adapter.getContent(USER_ID);

        // then
        // exception expected
    }

    @Test
    public void shouldGetContentReturnWithFailureInCaseOfBridgeFailure() throws CommunicationFailureException {

        // given
        doThrow(CommunicationFailureException.class).when(userBridgeService).getUserByID(USER_ID);

        // when
        Optional<ExtendedUserDataModel> result = adapter.getContent(USER_ID);

        // then
        assertThat(result.isPresent(), is(false));
        verify(userBridgeService).getUserByID(USER_ID);
    }

    @Test
    public void shouldGetIdentifierReturnProperValue() {

        // when
        ContentRequestAdapterIdentifier result = adapter.getIdentifier();

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(ContentRequestAdapterIdentifier.PROFILE_BASE_INFO));
    }
}
