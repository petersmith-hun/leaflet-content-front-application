package hu.psprog.leaflet.lcfa.core.utility;

import hu.psprog.leaflet.lcfa.core.config.PageConfigModel;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link ResourcePathResolver}.
 *
 * @author Peter Smith
 */
@RunWith(JUnitParamsRunner.class)
public class ResourcePathResolverTest {

    private static final String RESOURCE_SERVER_URL = "file:///resource-svc";
    private static final String EXPECTED_RESOLVED_RESOURCE_URL = "file:///resource-svc/res-01";
    private static final String UNRESOLVED_MARKDOWN = "this is a markdown source [with a resource]({resource-server-url}/res-01)";
    private static final String EXPECTED_RESOLVED_MARKDOWN = "this is a markdown source [with a resource](file:///resource-svc/res-01)";

    @Mock
    private PageConfigModel pageConfigModel;

    private ResourcePathResolver resourcePathResolver;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        given(pageConfigModel.getResourceServerUrl()).willReturn(RESOURCE_SERVER_URL);
        resourcePathResolver = new ResourcePathResolver(pageConfigModel);
    }

    @Test
    @Parameters({"/res-01", "res-01"})
    public void shouldResolve(String resourceID) {

        // when
        String result = resourcePathResolver.resolve(resourceID);

        // then
        assertThat(result, equalTo(EXPECTED_RESOLVED_RESOURCE_URL));
    }

    @Test
    public void shouldResolveInMarkdownSource() {

        // when
        String result = resourcePathResolver.resolveInMarkdownSource(UNRESOLVED_MARKDOWN);

        // then
        assertThat(result, equalTo(EXPECTED_RESOLVED_MARKDOWN));
    }
}
