package hu.psprog.leaflet.lcfa.web.config.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Application startup finished listener.
 *
 * @author Peter Smith
 */
@Component
public class ApplicationStartupFinishedListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationStartupFinishedListener.class);
    private static final String APP_VERSION = "${app.version}";
    private static final String APP_BUILD_DATE_PROPERTY = "${app.built}";

    private String appVersion;
    private String builtOn;

    @Autowired
    public ApplicationStartupFinishedListener(@Value(APP_VERSION) String appVersion, @Value(APP_BUILD_DATE_PROPERTY) String builtOn) {
        this.appVersion = appVersion;
        this.builtOn = builtOn;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        LOGGER.info("Application loaded successfully, running version v{}, built on {}", appVersion, builtOn);
    }
}
