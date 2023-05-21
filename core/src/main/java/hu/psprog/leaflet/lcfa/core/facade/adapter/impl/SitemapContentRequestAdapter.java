package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.api.rest.response.sitemap.Sitemap;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.exception.DefaultNonSuccessfulResponseException;
import hu.psprog.leaflet.bridge.service.SitemapBridgeService;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapter;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * {@link ContentRequestAdapter} implementation to retrieve sitemap.
 *
 * @author Peter Smith
 */
@Component
public class SitemapContentRequestAdapter implements ContentRequestAdapter<Sitemap, Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SitemapContentRequestAdapter.class);

    private final SitemapBridgeService sitemapBridgeService;

    @Autowired
    public SitemapContentRequestAdapter(SitemapBridgeService sitemapBridgeService) {
        this.sitemapBridgeService = sitemapBridgeService;
    }

    @Override
    public Optional<Sitemap> getContent(Void contentRequestParameter) {

        Sitemap sitemap = null;
        try {
            sitemap = sitemapBridgeService.getSitemap();
        } catch (DefaultNonSuccessfulResponseException | CommunicationFailureException e) {
            LOGGER.error("Failed to retrieve sitemap.", e);
        }

        return Optional.ofNullable(sitemap);
    }

    @Override
    public ContentRequestAdapterIdentifier getIdentifier() {
        return ContentRequestAdapterIdentifier.SITEMAP;
    }
}
