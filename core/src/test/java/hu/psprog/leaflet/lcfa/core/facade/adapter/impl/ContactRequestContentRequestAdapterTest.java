package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.service.ContactBridgeService;
import hu.psprog.leaflet.lcfa.core.converter.ContactRequestModelConverter;
import hu.psprog.leaflet.lcfa.core.domain.request.ContactRequestModel;
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
 * Unit tests for {@link ContactRequestContentRequestAdapter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class ContactRequestContentRequestAdapterTest {

    private static final ContactRequestModel CONTACT_REQUEST_MODEL = new ContactRequestModel();
    private static final hu.psprog.leaflet.api.rest.request.contact.ContactRequestModel CONVERTED_CONTACT_REQUEST_MODEL =
            new hu.psprog.leaflet.api.rest.request.contact.ContactRequestModel();
    private static final String RECAPTCHA_TOKEN = "recaptcha-token";

    static {
        CONTACT_REQUEST_MODEL.setRecaptchaToken(RECAPTCHA_TOKEN);
    }

    @Mock
    private ContactBridgeService contactBridgeService;

    @Mock
    private ContactRequestModelConverter contactRequestModelConverter;

    @InjectMocks
    private ContactRequestContentRequestAdapter adapter;

    @Test
    public void shouldGetContentReturnWithSuccess() throws CommunicationFailureException {

        // given
        given(contactRequestModelConverter.convert(CONTACT_REQUEST_MODEL)).willReturn(CONVERTED_CONTACT_REQUEST_MODEL);

        // when
        Optional<Boolean> result = adapter.getContent(CONTACT_REQUEST_MODEL);

        // then
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(true));
        verify(contactBridgeService).sendContactRequest(CONVERTED_CONTACT_REQUEST_MODEL, RECAPTCHA_TOKEN);
    }

    @Test
    public void shouldGetContentReturnWithFailureInCaseOfBridgeFailure() throws CommunicationFailureException {

        // given
        given(contactRequestModelConverter.convert(CONTACT_REQUEST_MODEL)).willReturn(CONVERTED_CONTACT_REQUEST_MODEL);
        doThrow(CommunicationFailureException.class).when(contactBridgeService).sendContactRequest(CONVERTED_CONTACT_REQUEST_MODEL, RECAPTCHA_TOKEN);

        // when
        Optional<Boolean> result = adapter.getContent(CONTACT_REQUEST_MODEL);

        // then
        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void shouldGetIdentifierReturnProperValue() {

        // when
        ContentRequestAdapterIdentifier result = adapter.getIdentifier();

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(ContentRequestAdapterIdentifier.CONTACT_REQUEST));
    }
}
