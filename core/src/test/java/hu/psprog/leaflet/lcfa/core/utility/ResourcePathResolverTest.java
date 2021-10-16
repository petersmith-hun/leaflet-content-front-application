package hu.psprog.leaflet.lcfa.core.utility;

import hu.psprog.leaflet.lcfa.core.config.PageConfigModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link ResourcePathResolver}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class ResourcePathResolverTest {

    private static final String RESOURCE_SERVER_URL = "file:///resource-svc";
    private static final String EXPECTED_RESOLVED_RESOURCE_URL = "file:///resource-svc/res-01";
    private static final String UNRESOLVED_MARKDOWN = "this is a markdown source [with a resource]({resource-server-url}/res-01)";
    private static final String EXPECTED_RESOLVED_MARKDOWN = "this is a markdown source [with a resource](file:///resource-svc/res-01)";

    @Mock
    private PageConfigModel pageConfigModel;

    @InjectMocks
    private ResourcePathResolver resourcePathResolver;

    @BeforeEach
    public void setup() {
        given(pageConfigModel.getResourceServerUrl()).willReturn(RESOURCE_SERVER_URL);
        resourcePathResolver = new ResourcePathResolver(pageConfigModel);
    }

    @ParameterizedTest
    @ValueSource(strings = {"/res-01", "res-01"})
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
