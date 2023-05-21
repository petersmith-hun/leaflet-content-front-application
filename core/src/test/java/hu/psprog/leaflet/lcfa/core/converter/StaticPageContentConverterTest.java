package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.document.DocumentDataModel;
import hu.psprog.leaflet.lcfa.core.domain.common.SEOAttributes;
import hu.psprog.leaflet.lcfa.core.domain.content.StaticPage;
import hu.psprog.leaflet.lcfa.core.domain.content.StaticPageContent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link StaticPageContentConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class StaticPageContentConverterTest {

    private static final String DOCUMENT_TITLE = "document title";
    private static final String RAW_CONTENT = "raw-content";
    private static final DocumentDataModel DOCUMENT_DATA_MODEL = DocumentDataModel.getBuilder()
            .withTitle(DOCUMENT_TITLE)
            .withRawContent(RAW_CONTENT)
            .build();
    private static final WrapperBodyDataModel<DocumentDataModel> WRAPPER_BODY_DATA_MODEL = WrapperBodyDataModel.<DocumentDataModel>getBuilder()
            .withBody(DOCUMENT_DATA_MODEL)
            .build();
    private static final SEOAttributes SEO_ATTRIBUTES = SEOAttributes.builder().pageTitle("page-title").build();
    private static final StaticPageContent EXPECTED_STATIC_PAGE_CONTENT = StaticPageContent.builder()
            .page(new StaticPage(DOCUMENT_TITLE, RAW_CONTENT))
            .seo(SEO_ATTRIBUTES)
            .build();

    @Mock
    private WrappedDataExtractor wrappedDataExtractor;

    @InjectMocks
    private StaticPageContentConverter staticPageContentConverter;

    @Test
    public void shouldConvert() {

        // given
        given(wrappedDataExtractor.extractSEOAttributes(WRAPPER_BODY_DATA_MODEL)).willReturn(SEO_ATTRIBUTES);

        // when
        StaticPageContent result = staticPageContentConverter.convert(WRAPPER_BODY_DATA_MODEL);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(EXPECTED_STATIC_PAGE_CONTENT));
    }
}
