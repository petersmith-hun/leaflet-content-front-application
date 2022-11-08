package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import hu.psprog.leaflet.lens.api.domain.MailRequestWrapper;
import hu.psprog.leaflet.lens.api.domain.SystemStartup;
import hu.psprog.leaflet.lens.client.EventNotificationServiceClient;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link SystemStartupNotificationContentRequestAdapter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class SystemStartupNotificationContentRequestAdapterTest {

    private static final String VERSION = "1.0.0";
    private static final String APPLICATION_NAME = "Leaflet Content Front Application";
    private static final String SYSTEM_STARTUP_SUBJECT_KEY = "mail.system.event.startup.subject.lcfa";

    @Mock
    private EventNotificationServiceClient eventNotificationServiceClient;

    @InjectMocks
    private SystemStartupNotificationContentRequestAdapter systemStartupNotificationContentRequestAdapter;

    @Test
    public void shouldGetContentSubmitNotification() throws CommunicationFailureException {

        // given
        var expectedWrapper = MailRequestWrapper.<SystemStartup>builder()
                .overrideSubjectKey(SYSTEM_STARTUP_SUBJECT_KEY)
                .content(SystemStartup.builder()
                        .applicationName(APPLICATION_NAME)
                        .version(VERSION)
                        .build())
                .build();

        // when
        Optional<Boolean> result = systemStartupNotificationContentRequestAdapter.getContent(VERSION);

        // then
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(true));

        verify(eventNotificationServiceClient).requestMailNotification(expectedWrapper);
    }

    @Test
    public void shouldGetContentFailSilentlyOnAnyException() throws CommunicationFailureException {

        // given
        doThrow(CommunicationFailureException.class).when(eventNotificationServiceClient).requestMailNotification(any(MailRequestWrapper.class));

        // when
        Optional<Boolean> result = systemStartupNotificationContentRequestAdapter.getContent(VERSION);

        // then
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(false));
    }

    @Test
    public void shouldGetIdentifierReturnProperValue() {

        // when
        ContentRequestAdapterIdentifier result = systemStartupNotificationContentRequestAdapter.getIdentifier();

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(ContentRequestAdapterIdentifier.SYSTEM_STARTUP_NOTIFICATION));
    }
}
