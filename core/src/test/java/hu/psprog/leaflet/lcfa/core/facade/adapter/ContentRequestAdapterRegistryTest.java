package hu.psprog.leaflet.lcfa.core.facade.adapter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link ContentRequestAdapterRegistry}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class ContentRequestAdapterRegistryTest {

    @Mock
    private ContentRequestAdapter<String, String> mockedArticleContentRequestAdapter;

    @Mock
    private ContentRequestAdapter<String, String> mockedStaticPageContentRequestAdapter;

    @Mock
    private ContentRequestAdapter<String, String> mockedContactRequestContentRequestAdapter;

    private ContentRequestAdapterRegistry contentRequestAdapterRegistry;

    @BeforeEach
    public void setup() {
        given(mockedArticleContentRequestAdapter.getIdentifier()).willReturn(ContentRequestAdapterIdentifier.ARTICLE);
        given(mockedStaticPageContentRequestAdapter.getIdentifier()).willReturn(ContentRequestAdapterIdentifier.STATIC_PAGE);
        given(mockedContactRequestContentRequestAdapter.getIdentifier()).willReturn(ContentRequestAdapterIdentifier.CONTACT_REQUEST);
        contentRequestAdapterRegistry = new ContentRequestAdapterRegistry(Arrays.asList(
                mockedArticleContentRequestAdapter, mockedStaticPageContentRequestAdapter, mockedContactRequestContentRequestAdapter));
    }

    @Test
    public void shouldGetContentRequestAdapterReturnWithProperAdapterImplementation() {

        // when
        ContentRequestAdapter<String, String> result = contentRequestAdapterRegistry.getContentRequestAdapter(ContentRequestAdapterIdentifier.STATIC_PAGE);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(mockedStaticPageContentRequestAdapter));
    }

    @Test
    public void shouldGetContentRequestAdapterThrowIllegalArgumentExceptionForNonExistingMapping() {

        // when
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> contentRequestAdapterRegistry.getContentRequestAdapter(ContentRequestAdapterIdentifier.CONTENT_FILTER));

        // then
        // exception expected
    }

    @Test
    public void shouldAssertMappingDoNothingForAdequateMapping() throws Exception {

        // given
        replaceMappingWithMock();

        // when
        contentRequestAdapterRegistry.afterPropertiesSet();

        // then
        // do nothing
    }

    @Test
    public void shouldAssertMappingsThrowIllegalStateExceptionForDeficientMapping() {

        // when
        Assertions.assertThrows(IllegalStateException.class, () -> contentRequestAdapterRegistry.afterPropertiesSet());

        // then
        // exception expected
    }

    private void replaceMappingWithMock() throws IllegalAccessException {
        Map<ContentRequestAdapterIdentifier, ContentRequestAdapter<?, ?>> contentRequestAdapterMapMock = Mockito.mock(Map.class);
        Set<ContentRequestAdapterIdentifier> contentRequestAdapterIdentifierSetMock = Mockito.mock(Set.class);

        given(contentRequestAdapterMapMock.keySet()).willReturn(contentRequestAdapterIdentifierSetMock);
        given(contentRequestAdapterIdentifierSetMock.size()).willReturn(19);

        Field contentRequestAdapterMapField = ReflectionUtils.findField(ContentRequestAdapterRegistry.class, "contentRequestAdapterMap");
        contentRequestAdapterMapField.setAccessible(true);
        contentRequestAdapterMapField.set(contentRequestAdapterRegistry, contentRequestAdapterMapMock);
    }
}
