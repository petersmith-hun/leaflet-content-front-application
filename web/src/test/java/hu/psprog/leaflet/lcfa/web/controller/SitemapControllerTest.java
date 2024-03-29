package hu.psprog.leaflet.lcfa.web.controller;

import hu.psprog.leaflet.api.rest.response.sitemap.Sitemap;
import hu.psprog.leaflet.api.rest.response.sitemap.SitemapLocationItem;
import hu.psprog.leaflet.lcfa.core.facade.CommonPageDataFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link SitemapController}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class SitemapControllerTest {

    private static final Sitemap SITEMAP = Sitemap.getBuilder()
            .withSitemapLocationItemList(List.of(new SitemapLocationItem("/location/test")))
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
