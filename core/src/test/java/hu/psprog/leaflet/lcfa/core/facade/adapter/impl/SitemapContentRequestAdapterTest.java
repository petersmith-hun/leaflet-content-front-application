package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.api.rest.response.sitemap.Sitemap;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.service.SitemapBridgeService;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

/**
 * Unit tests for {@link SitemapContentRequestAdapter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class SitemapContentRequestAdapterTest {

    private static final Sitemap SITEMAP = Sitemap.getBuilder()
            .withLocation("/location/test")
            .build();

    @Mock
    private SitemapBridgeService sitemapBridgeService;

    @InjectMocks
    private SitemapContentRequestAdapter adapter;

    @Test
    public void shouldGetContentReturnWithSuccess() throws CommunicationFailureException {

        // given
        given(sitemapBridgeService.getSitemap()).willReturn(SITEMAP);

        // when
        Optional<Sitemap> result = adapter.getContent(null);

        // then
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), equalTo(SITEMAP));
    }

    @Test
    public void shouldGetContentReturnWithEmptyResponseInCaseOfBridgeFailure() throws CommunicationFailureException {

        // given
        doThrow(CommunicationFailureException.class).when(sitemapBridgeService).getSitemap();

        // when
        Optional<Sitemap> result = adapter.getContent(null);

        // then
        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void shouldGetIdentifierReturnProperValue() {

        // when
        ContentRequestAdapterIdentifier result = adapter.getIdentifier();

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(ContentRequestAdapterIdentifier.SITEMAP));
    }
}
