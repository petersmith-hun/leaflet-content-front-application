package hu.psprog.leaflet.lcfa.web.controller;

import hu.psprog.leaflet.api.rest.response.sitemap.Sitemap;
import hu.psprog.leaflet.lcfa.core.facade.CommonPageDataFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * Controller implementation that renders sitemap as XML document.
 * Format is compatible with Google Search Console requirements.
 *
 * @author Peter Smith
 */
@RestController
public class SitemapController {

    private static final String PATH_SITEMAP = "/sitemap.xml";

    private final CommonPageDataFacade commonPageDataFacade;

    @Autowired
    public SitemapController(CommonPageDataFacade commonPageDataFacade) {
        this.commonPageDataFacade = commonPageDataFacade;
    }

    /**
     * GET /sitemap.xml
     * Renders sitemap.
     *
     * @return populated sitemap as XML document
     */
    @GetMapping(path = PATH_SITEMAP, produces = APPLICATION_XML_VALUE, consumes = ALL_VALUE)
    public ResponseEntity<Sitemap> renderSitemap() {
        return ResponseEntity.ok(commonPageDataFacade.getSitemap());
    }
}
