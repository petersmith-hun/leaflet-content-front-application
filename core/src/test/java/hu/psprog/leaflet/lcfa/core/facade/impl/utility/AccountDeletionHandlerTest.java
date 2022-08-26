package hu.psprog.leaflet.lcfa.core.facade.impl.utility;

import hu.psprog.leaflet.api.rest.response.user.ExtendedUserDataModel;
import hu.psprog.leaflet.lcfa.core.exception.UserRequestProcessingException;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapter;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterRegistry;
import hu.psprog.leaflet.lcfa.core.mock.WithMockedJWTUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

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
    private static final ExtendedUserDataModel EXTENDED_USER_DATA_MODEL = ExtendedUserDataModel.getExtendedBuilder()
            .withId(USER_ID)
            .withEmail(EMAIL)
            .build();

    @Mock
    private ContentRequestAdapterRegistry contentRequestAdapterRegistry;

    @Mock
    private ContentRequestAdapter<ExtendedUserDataModel, Long> userDataContentRequestAdapter;

    @Mock
    private ContentRequestAdapter<Boolean, Long> accountDeletionContentRequestAdapter;

    private AccountDeletionHandler accountDeletionHandler;

    @BeforeEach
    public void setup() {
        accountDeletionHandler = new AccountDeletionHandler(contentRequestAdapterRegistry);
    }

    @Test
    @WithMockedJWTUser
    public void shouldDeleteAccountReturnWithSuccess() {

        // given
        given(contentRequestAdapterRegistry.<ExtendedUserDataModel, Long>getContentRequestAdapter(ContentRequestAdapterIdentifier.PROFILE_BASE_INFO))
                .willReturn(userDataContentRequestAdapter);
        given(userDataContentRequestAdapter.getContent(USER_ID)).willReturn(Optional.of(EXTENDED_USER_DATA_MODEL));
        given(contentRequestAdapterRegistry.<Boolean, Long>getContentRequestAdapter(ContentRequestAdapterIdentifier.PROFILE_DELETE))
                .willReturn(accountDeletionContentRequestAdapter);
        given(accountDeletionContentRequestAdapter.getContent(USER_ID)).willReturn(Optional.of(true));

        // when
        boolean result = accountDeletionHandler.deleteAccount(USER_ID);

        // then
        assertThat(result, is(true));
        assertThat(SecurityContextHolder.getContext().getAuthentication(), nullValue());
    }

    @Test
    @WithMockedJWTUser
    public void shouldDeleteAccountThrowUserRequestProcessingExceptionForMissingUserID() {

        // when
        Assertions.assertThrows(UserRequestProcessingException.class, () -> accountDeletionHandler.deleteAccount(null));

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
        boolean result = accountDeletionHandler.deleteAccount(USER_ID);

        // then
        assertThat(result, is(false));
        assertThat(SecurityContextHolder.getContext().getAuthentication(), notNullValue());
    }

    @Test
    @WithMockedJWTUser
    public void shouldDeleteAccountReturnWithFailureForFailedReAuthentication() {

        // given
        given(contentRequestAdapterRegistry.<ExtendedUserDataModel, Long>getContentRequestAdapter(ContentRequestAdapterIdentifier.PROFILE_BASE_INFO))
                .willReturn(userDataContentRequestAdapter);
        given(userDataContentRequestAdapter.getContent(USER_ID)).willReturn(Optional.empty());

        // when
        boolean result = accountDeletionHandler.deleteAccount(USER_ID);

        // then
        assertThat(result, is(false));
        assertThat(SecurityContextHolder.getContext().getAuthentication(), notNullValue());
    }

    @Test
    @WithMockedJWTUser
    public void shouldDeleteAccountReturnWithFailureForMissingDeletionData() {

        // given
        given(contentRequestAdapterRegistry.<ExtendedUserDataModel, Long>getContentRequestAdapter(ContentRequestAdapterIdentifier.PROFILE_BASE_INFO))
                .willReturn(userDataContentRequestAdapter);
        given(userDataContentRequestAdapter.getContent(USER_ID)).willReturn(Optional.of(EXTENDED_USER_DATA_MODEL));
        given(contentRequestAdapterRegistry.<Boolean, Long>getContentRequestAdapter(ContentRequestAdapterIdentifier.PROFILE_DELETE))
                .willReturn(accountDeletionContentRequestAdapter);
        given(accountDeletionContentRequestAdapter.getContent(USER_ID)).willReturn(Optional.empty());

        // when
        boolean result = accountDeletionHandler.deleteAccount(USER_ID);

        // then
        assertThat(result, is(false));
        assertThat(SecurityContextHolder.getContext().getAuthentication(), notNullValue());
    }

    @Test
    @WithMockedJWTUser
    public void shouldDeleteAccountStillForciblyLogOutUserIfSecondRevokeCallFails() {

        // given
        given(contentRequestAdapterRegistry.<ExtendedUserDataModel, Long>getContentRequestAdapter(ContentRequestAdapterIdentifier.PROFILE_BASE_INFO))
                .willReturn(userDataContentRequestAdapter);
        given(userDataContentRequestAdapter.getContent(USER_ID)).willReturn(Optional.of(EXTENDED_USER_DATA_MODEL));
        given(contentRequestAdapterRegistry.<Boolean, Long>getContentRequestAdapter(ContentRequestAdapterIdentifier.PROFILE_DELETE))
                .willReturn(accountDeletionContentRequestAdapter);
        given(accountDeletionContentRequestAdapter.getContent(USER_ID)).willReturn(Optional.of(true));

        // when
        boolean result = accountDeletionHandler.deleteAccount(USER_ID);

        // then
        assertThat(result, is(true));
        assertThat(SecurityContextHolder.getContext().getAuthentication(), nullValue());
    }
}
