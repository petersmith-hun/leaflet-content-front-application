package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.exception.UnauthorizedAccessException;
import hu.psprog.leaflet.bridge.service.UserBridgeService;
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
 * Unit tests for {@link UserAccountDeletionContentRequestAdapter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class UserAccountDeletionContentRequestAdapterTest {

    private static final long USER_ID = 1L;
    @Mock
    private UserBridgeService userBridgeService;

    @InjectMocks
    private UserAccountDeletionContentRequestAdapter adapter;

    @Test
    public void shouldGetContentReturnWithSuccess() throws CommunicationFailureException {

        // when
        Optional<Boolean> result = adapter.getContent(USER_ID);

        // then
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(true));
        verify(userBridgeService).deleteUser(USER_ID);
    }

    @Test
    public void shouldGetContentReturnThrowUserSessionInvalidationRequiredException() throws CommunicationFailureException {

        // given
        doThrow(UnauthorizedAccessException.class).when(userBridgeService).deleteUser(USER_ID);

        // when
        Assertions.assertThrows(UserSessionInvalidationRequiredException.class, () ->adapter.getContent(USER_ID));

        // then
        // exception expected
    }

    @Test
    public void shouldGetContentReturnWithFailureInCaseOfBridgeFailure() throws CommunicationFailureException {

        // given
        doThrow(CommunicationFailureException.class).when(userBridgeService).deleteUser(USER_ID);

        // when
        Optional<Boolean> result = adapter.getContent(USER_ID);

        // then
        assertThat(result.isPresent(), is(false));
        verify(userBridgeService).deleteUser(USER_ID);
    }

    @Test
    public void shouldGetIdentifierReturnProperValue() {

        // when
        ContentRequestAdapterIdentifier result = adapter.getIdentifier();

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(ContentRequestAdapterIdentifier.PROFILE_DELETE));
    }
}
