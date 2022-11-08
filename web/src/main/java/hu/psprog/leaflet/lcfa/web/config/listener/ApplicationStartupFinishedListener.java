package hu.psprog.leaflet.lcfa.web.config.listener;

import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * Application startup finished listener.
 *
 * @author Peter Smith
 */
@Component
public class ApplicationStartupFinishedListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationStartupFinishedListener.class);
    private static final String UNKNOWN_BUILD_TIME = "unknown";

    private final Optional<BuildProperties> optionalBuildProperties;
    private final ContentRequestAdapter<Boolean, String> notificationRequestAdapter;

    public ApplicationStartupFinishedListener(@Autowired(required = false) Optional<BuildProperties> buildProperties,
                                              @Qualifier("systemStartupNotificationContentRequestAdapter") ContentRequestAdapter<Boolean, String> notificationRequestAdapter) {
        this.optionalBuildProperties = buildProperties;
        this.notificationRequestAdapter = notificationRequestAdapter;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        optionalBuildProperties.ifPresent(buildProperties -> {
            LOGGER.info("Application loaded successfully, running version v{}, built on {}", buildProperties.getVersion(), getBuildTime(buildProperties));
            notificationRequestAdapter.getContent(buildProperties.getVersion())
                    .ifPresent(submitted -> {
                        if (submitted) {
                            LOGGER.info("Submitted system startup notification");
                        } else {
                            LOGGER.warn("Failed to submit system startup notification");
                        }
                    });
        });
    }

    private String getBuildTime(BuildProperties buildProperties) {
        return Optional.ofNullable(buildProperties.getTime())
                .map(buildTime -> buildTime.atZone(ZoneId.systemDefault()))
                .map(ZonedDateTime::toString)
                .orElse(UNKNOWN_BUILD_TIME);
    }
}
