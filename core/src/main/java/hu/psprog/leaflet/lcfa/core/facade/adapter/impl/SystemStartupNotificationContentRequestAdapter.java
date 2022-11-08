package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapter;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import hu.psprog.leaflet.lens.api.domain.MailRequestWrapper;
import hu.psprog.leaflet.lens.api.domain.SystemStartup;
import hu.psprog.leaflet.lens.client.EventNotificationServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * {@link ContentRequestAdapter} implementation for submitting a system startup notification via LENS.
 *
 * @author Peter Smith
 */
@Component
public class SystemStartupNotificationContentRequestAdapter implements ContentRequestAdapter<Boolean, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemStartupNotificationContentRequestAdapter.class);

    private static final String APPLICATION_NAME = "Leaflet Content Front Application";
    private static final String SYSTEM_STARTUP_SUBJECT_KEY = "mail.system.event.startup.subject.lcfa";

    private final EventNotificationServiceClient eventNotificationServiceClient;

    @Autowired
    public SystemStartupNotificationContentRequestAdapter(EventNotificationServiceClient eventNotificationServiceClient) {
        this.eventNotificationServiceClient = eventNotificationServiceClient;
    }

    @Override
    public Optional<Boolean> getContent(String version) {

        boolean notificationSent = false;
        try {
            var systemStartupMail = MailRequestWrapper.<SystemStartup>builder()
                    .overrideSubjectKey(SYSTEM_STARTUP_SUBJECT_KEY)
                    .content(SystemStartup.builder()
                            .applicationName(APPLICATION_NAME)
                            .version(version)
                            .build())
                    .build();

            eventNotificationServiceClient.requestMailNotification(systemStartupMail);
            notificationSent = true;

        } catch (Exception exception) {
            LOGGER.error("Failed to submit system startup notification", exception);
        }

        return Optional.of(notificationSent);
    }

    @Override
    public ContentRequestAdapterIdentifier getIdentifier() {
        return ContentRequestAdapterIdentifier.SYSTEM_STARTUP_NOTIFICATION;
    }
}
