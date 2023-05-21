package hu.psprog.leaflet.lcfa.web.config;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Static resource mapping.
 *
 * @author Peter Smith
 */
@Data
@Setter(AccessLevel.PACKAGE)
@Component
@ConfigurationProperties(prefix = "webapp")
public class WebAppResources {

    private List<WebAppResource> resources = new ArrayList<>();

    @Data
    @Setter(AccessLevel.PACKAGE)
    public static class WebAppResource {

        private String handler;
        private String location;

    }
}
