package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.api.rest.request.user.PasswordResetDemandRequestModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.service.UserBridgeService;
import hu.psprog.leaflet.lcfa.core.domain.request.PasswordReclaimRequestModel;
import hu.psprog.leaflet.lcfa.core.domain.result.PasswordResetResult;
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
 * Unit tests for {@link PasswordResetRequestContentRequestAdapter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class PasswordResetRequestContentRequestAdapterTest {

    private static final PasswordReclaimRequestModel PASSWORD_RECLAIM_REQUEST_MODEL = new PasswordReclaimRequestModel();
    private static final PasswordResetDemandRequestModel PASSWORD_RESET_DEMAND_REQUEST_MODEL = new PasswordResetDemandRequestModel();
    private static final String RECAPTCHA_TOKEN = "recaptcha-token";

    private static final String EMAIL = "email";

    static {
        PASSWORD_RECLAIM_REQUEST_MODEL.setRecaptchaToken(RECAPTCHA_TOKEN);
        PASSWORD_RECLAIM_REQUEST_MODEL.setEmail(EMAIL);
        PASSWORD_RESET_DEMAND_REQUEST_MODEL.setEmail(EMAIL);
    }

    @Mock
    private UserBridgeService userBridgeService;

    @InjectMocks
    private PasswordResetRequestContentRequestAdapter adapter;

    @Test
    public void shouldGetContentReturnWithDemandProcessedStatus() throws CommunicationFailureException {

        // when
        Optional<PasswordResetResult> result = adapter.getContent(PASSWORD_RECLAIM_REQUEST_MODEL);

        // then
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), equalTo(PasswordResetResult.DEMAND_PROCESSED));
        verify(userBridgeService).demandPasswordReset(PASSWORD_RESET_DEMAND_REQUEST_MODEL, RECAPTCHA_TOKEN);
    }

    @Test
    public void shouldGetContentReturnWithDemandFailedStatusInCaseOfBridgeFailure() throws CommunicationFailureException {

        // given
        doThrow(CommunicationFailureException.class).when(userBridgeService).demandPasswordReset(PASSWORD_RESET_DEMAND_REQUEST_MODEL, RECAPTCHA_TOKEN);

        // when
        Optional<PasswordResetResult> result = adapter.getContent(PASSWORD_RECLAIM_REQUEST_MODEL);

        // then
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), equalTo(PasswordResetResult.DEMAND_FAILED));
        verify(userBridgeService).demandPasswordReset(PASSWORD_RESET_DEMAND_REQUEST_MODEL, RECAPTCHA_TOKEN);
    }

    @Test
    public void shouldGetIdentifierReturnProperValue() {

        // when
        ContentRequestAdapterIdentifier result = adapter.getIdentifier();

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(ContentRequestAdapterIdentifier.PASSWORD_RESET_REQUEST));
    }
}
