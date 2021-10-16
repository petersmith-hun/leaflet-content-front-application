package hu.psprog.leaflet.lcfa.core.facade.impl.utility;

import hu.psprog.leaflet.api.rest.response.user.ExtendedUserDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.exception.UnauthorizedAccessException;
import hu.psprog.leaflet.jwt.auth.support.service.AuthenticationService;
import hu.psprog.leaflet.lcfa.core.domain.request.AccountDeletionRequest;
import hu.psprog.leaflet.lcfa.core.exception.UserRequestProcessingException;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapter;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterRegistry;
import hu.psprog.leaflet.lcfa.core.mock.WithMockedJWTUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * Unit tests for {@link AccountDeletionHandler}.
 *
 * @author Peter Smith
 */
@Extensions({
        @ExtendWith(MockitoExtension.class),
        @ExtendWith(SpringExtension.class)
})
public class AccountDeletionHandlerTest {

    private static final long USER_ID = 1L;
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final AccountDeletionRequest ACCOUNT_DELETION_REQUEST = new AccountDeletionRequest();
    private static final ExtendedUserDataModel EXTENDED_USER_DATA_MODEL = ExtendedUserDataModel.getExtendedBuilder()
            .withId(USER_ID)
            .withEmail(EMAIL)
            .build();
    private static final Authentication RECLAIM_AUTHENTICATION = new UsernamePasswordAuthenticationToken(EMAIL, PASSWORD);

    static {
        ACCOUNT_DELETION_REQUEST.setPassword(PASSWORD);
    }

    @Mock
    private ContentRequestAdapterRegistry contentRequestAdapterRegistry;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private ContentRequestAdapter<ExtendedUserDataModel, Long> userDataContentRequestAdapter;

    @Mock
    private ContentRequestAdapter<Boolean, Long> accountDeletionContentRequestAdapter;

    @Mock
    private Authentication reAuthenticationResultAuthentication;

    @InjectMocks
    private AccountDeletionHandler accountDeletionHandler;

    @Test
    @WithMockedJWTUser
    public void shouldDeleteAccountReturnWithSuccess() throws CommunicationFailureException {

        // given
        given(contentRequestAdapterRegistry.<ExtendedUserDataModel, Long>getContentRequestAdapter(ContentRequestAdapterIdentifier.PROFILE_BASE_INFO))
                .willReturn(userDataContentRequestAdapter);
        given(userDataContentRequestAdapter.getContent(USER_ID)).willReturn(Optional.of(EXTENDED_USER_DATA_MODEL));
        given(authenticationService.claimToken(RECLAIM_AUTHENTICATION)).willReturn(reAuthenticationResultAuthentication);
        given(contentRequestAdapterRegistry.<Boolean, Long>getContentRequestAdapter(ContentRequestAdapterIdentifier.PROFILE_DELETE))
                .willReturn(accountDeletionContentRequestAdapter);
        given(accountDeletionContentRequestAdapter.getContent(USER_ID)).willReturn(Optional.of(true));

        // when
        boolean result = accountDeletionHandler.deleteAccount(USER_ID, ACCOUNT_DELETION_REQUEST);

        // then
        assertThat(result, is(true));
        assertThat(SecurityContextHolder.getContext().getAuthentication(), nullValue());
        verify(authenticationService, times(2)).revokeToken();
    }

    @Test
    @WithMockedJWTUser
    public void shouldDeleteAccountThrowUserRequestProcessingExceptionForMissingUserID() {

        // when
        Assertions.assertThrows(UserRequestProcessingException.class, () -> accountDeletionHandler.deleteAccount(null, ACCOUNT_DELETION_REQUEST));

        // then
        // exception expected
    }

    @Test
    @WithMockedJWTUser
    public void shouldDeleteAccountReturnWithFailureForMissingUserData() {

        // given
        given(contentRequestAdapterRegistry.<ExtendedUserDataModel, Long>getContentRequestAdapter(ContentRequestAdapterIdentifier.PROFILE_BASE_INFO))
                .willReturn(userDataContentRequestAdapter);
        given(userDataContentRequestAdapter.getContent(USER_ID)).willReturn(Optional.empty());

        // when
        boolean result = accountDeletionHandler.deleteAccount(USER_ID, ACCOUNT_DELETION_REQUEST);

        // then
        assertThat(result, is(false));
        assertThat(SecurityContextHolder.getContext().getAuthentication(), notNullValue());
        verifyNoInteractions(authenticationService, accountDeletionContentRequestAdapter);
    }

    @Test
    @WithMockedJWTUser
    public void shouldDeleteAccountReturnWithFailureForFailedReAuthentication() throws CommunicationFailureException {

        // given
        given(contentRequestAdapterRegistry.<ExtendedUserDataModel, Long>getContentRequestAdapter(ContentRequestAdapterIdentifier.PROFILE_BASE_INFO))
                .willReturn(userDataContentRequestAdapter);
        given(userDataContentRequestAdapter.getContent(USER_ID)).willReturn(Optional.of(EXTENDED_USER_DATA_MODEL));
        doThrow(UnauthorizedAccessException.class).when(authenticationService).claimToken(RECLAIM_AUTHENTICATION);

        // when
        boolean result = accountDeletionHandler.deleteAccount(USER_ID, ACCOUNT_DELETION_REQUEST);

        // then
        assertThat(result, is(false));
        assertThat(SecurityContextHolder.getContext().getAuthentication(), notNullValue());
        verify(authenticationService, never()).revokeToken();
    }

    @Test
    @WithMockedJWTUser
    public void shouldDeleteAccountReturnWithFailureForMissingDeletionData() throws CommunicationFailureException {

        // given
        given(contentRequestAdapterRegistry.<ExtendedUserDataModel, Long>getContentRequestAdapter(ContentRequestAdapterIdentifier.PROFILE_BASE_INFO))
                .willReturn(userDataContentRequestAdapter);
        given(userDataContentRequestAdapter.getContent(USER_ID)).willReturn(Optional.of(EXTENDED_USER_DATA_MODEL));
        given(authenticationService.claimToken(RECLAIM_AUTHENTICATION)).willReturn(reAuthenticationResultAuthentication);
        given(contentRequestAdapterRegistry.<Boolean, Long>getContentRequestAdapter(ContentRequestAdapterIdentifier.PROFILE_DELETE))
                .willReturn(accountDeletionContentRequestAdapter);
        given(accountDeletionContentRequestAdapter.getContent(USER_ID)).willReturn(Optional.empty());

        // when
        boolean result = accountDeletionHandler.deleteAccount(USER_ID, ACCOUNT_DELETION_REQUEST);

        // then
        assertThat(result, is(false));
        assertThat(SecurityContextHolder.getContext().getAuthentication(), notNullValue());
        verify(authenticationService).revokeToken();
    }

    @Test
    @WithMockedJWTUser
    public void shouldDeleteAccountStillForciblyLogOutUserIfSecondRevokeCallFails() throws CommunicationFailureException {

        // given
        given(contentRequestAdapterRegistry.<ExtendedUserDataModel, Long>getContentRequestAdapter(ContentRequestAdapterIdentifier.PROFILE_BASE_INFO))
                .willReturn(userDataContentRequestAdapter);
        given(userDataContentRequestAdapter.getContent(USER_ID)).willReturn(Optional.of(EXTENDED_USER_DATA_MODEL));
        given(authenticationService.claimToken(RECLAIM_AUTHENTICATION)).willReturn(reAuthenticationResultAuthentication);
        given(contentRequestAdapterRegistry.<Boolean, Long>getContentRequestAdapter(ContentRequestAdapterIdentifier.PROFILE_DELETE))
                .willReturn(accountDeletionContentRequestAdapter);
        given(accountDeletionContentRequestAdapter.getContent(USER_ID)).willReturn(Optional.of(true));
        doNothing().doThrow(UnauthorizedAccessException.class).when(authenticationService).revokeToken();

        // when
        boolean result = accountDeletionHandler.deleteAccount(USER_ID, ACCOUNT_DELETION_REQUEST);

        // then
        assertThat(result, is(true));
        assertThat(SecurityContextHolder.getContext().getAuthentication(), nullValue());
        verify(authenticationService, times(2)).revokeToken();
    }
}
