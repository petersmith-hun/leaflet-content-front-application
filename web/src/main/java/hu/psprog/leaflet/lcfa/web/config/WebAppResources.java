package hu.psprog.leaflet.lcfa.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Static resource mapping.
 *
 * @author Peter Smith
 */
@Component
@ConfigurationProperties(prefix = "webapp")
public class WebAppResources {

    private List<WebAppResource> resources = new ArrayList<>();

    public List<WebAppResource> getResources() {
        return resources;
    }

    public void setResources(List<WebAppResource> resources) {
        this.resources = resources;
    }

    public static class WebAppResource {

        private String handler;
        private String location;

        public String getHandler() {
            return handler;
        }

        public void setHandler(String handler) {
            this.handler = handler;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }
    }
}
