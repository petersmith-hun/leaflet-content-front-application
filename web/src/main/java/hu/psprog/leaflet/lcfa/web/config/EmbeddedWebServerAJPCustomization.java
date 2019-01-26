package hu.psprog.leaflet.lcfa.web.config;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;

/**
 * Embedded Tomcat server customization.
 *
 * @author Peter Smith
 */
public class EmbeddedWebServerAJPCustomization implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    private static final String AJP_PROTOCOL = "AJP/1.3";

    private int ajpPort;

    public EmbeddedWebServerAJPCustomization(int ajpPort) {
        this.ajpPort = ajpPort;
    }

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        factory.setProtocol(AJP_PROTOCOL);
        factory.setPort(ajpPort);
    }
}
