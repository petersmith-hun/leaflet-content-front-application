package hu.psprog.leaflet.lcfa.web.config;

import org.apache.coyote.ProtocolHandler;
import org.apache.coyote.ajp.AbstractAjpProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;

/**
 * Embedded Tomcat server customization.
 *
 * @author Peter Smith
 */
public class EmbeddedWebServerAJPCustomization implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmbeddedWebServerAJPCustomization.class);

    private static final String AJP_PROTOCOL = "AJP/1.3";

    private final int ajpPort;

    public EmbeddedWebServerAJPCustomization(int ajpPort) {
        this.ajpPort = ajpPort;
    }

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        factory.setProtocol(AJP_PROTOCOL);
        factory.setPort(ajpPort);
        factory.addProtocolHandlerCustomizers(this::customizeProtocolHandler);
    }

    private void customizeProtocolHandler(ProtocolHandler protocolHandler) {

        if (protocolHandler instanceof AbstractAjpProtocol) {
            ((AbstractAjpProtocol<?>) protocolHandler).setSecretRequired(false);
        } else {
            LOGGER.warn("Initialized ProtocolHandler is not AJP - customization failed");
        }
    }
}
