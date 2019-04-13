package hu.psprog.leaflet.lcfa.web.controller;

import hu.psprog.leaflet.api.rest.response.sitemap.Sitemap;
import hu.psprog.leaflet.lcfa.core.facade.CommonPageDataFacade;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link SitemapController}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class SitemapControllerTest {

    private static final Sitemap SITEMAP = Sitemap.getBuilder()
            .withLocation("/location/test")
            .build();

    @Mock
    private CommonPageDataFacade commonPageDataFacade;

    @InjectMocks
    private SitemapController sitemapController;

    @Test
    public void shouldRenderSitemap() {

        // given
        given(commonPageDataFacade.getSitemap()).willReturn(SITEMAP);

        // when
        ResponseEntity<Sitemap> result = sitemapController.renderSitemap();

        // then
        assertThat(result, notNullValue());
        assertThat(result.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(result.getBody(), equalTo(SITEMAP));
    }
}
