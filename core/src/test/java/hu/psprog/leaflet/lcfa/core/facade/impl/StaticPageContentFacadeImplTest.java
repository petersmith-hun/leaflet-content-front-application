package hu.psprog.leaflet.lcfa.core.facade.impl;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.document.DocumentDataModel;
import hu.psprog.leaflet.lcfa.core.config.PageConfigModel;
import hu.psprog.leaflet.lcfa.core.domain.content.StaticPage;
import hu.psprog.leaflet.lcfa.core.domain.content.StaticPageContent;
import hu.psprog.leaflet.lcfa.core.domain.content.StaticPageType;
import hu.psprog.leaflet.lcfa.core.exception.ContentNotFoundException;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapter;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterRegistry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;

import java.util.Map;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link StaticPageContentFacadeImpl}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class StaticPageContentFacadeImplTest {

    private static final PageConfigModel PAGE_CONFIG_MODEL = new PageConfigModel();
    private static final String TITLE = "title";
    private static final String CONTENT = "content";
    private static final String INTRODUCTION_PAGE_LINK = "introduction";
    private static final WrapperBodyDataModel<DocumentDataModel> WRAPPED_DOCUMENT_DATA_MODEL = WrapperBodyDataModel.getBuilder()
            .withBody(DocumentDataModel.getBuilder()
                    .withTitle(TITLE)
                    .withRawContent(CONTENT)
                    .build())
            .build();
    private static final StaticPageContent STATIC_PAGE_CONTENT = StaticPageContent.builder()
            .page(new StaticPage(TITLE, CONTENT))
            .build();

    static {
        PAGE_CONFIG_MODEL.setStaticPageMapping(Map.of(StaticPageType.INTRODUCTION, INTRODUCTION_PAGE_LINK));
    }

    @Mock
    private ContentRequestAdapterRegistry contentRequestAdapterRegistry;

    @Mock
    private ConversionService conversionService;

    @Mock
    private ContentRequestAdapter<WrapperBodyDataModel<DocumentDataModel>, String> staticPageContentRequestAdapter;

    private StaticPageContentFacadeImpl staticPageContentFacade;

    @BeforeEach
    public void setup() {
        staticPageContentFacade = new StaticPageContentFacadeImpl(contentRequestAdapterRegistry, conversionService, PAGE_CONFIG_MODEL);
    }

    @Test
    public void shouldGetStaticPageWithSuccess() {

        // given
        given(contentRequestAdapterRegistry.<WrapperBodyDataModel<DocumentDataModel>, String>getContentRequestAdapter(ContentRequestAdapterIdentifier.STATIC_PAGE))
                .willReturn(staticPageContentRequestAdapter);
        given(staticPageContentRequestAdapter.getContent(INTRODUCTION_PAGE_LINK)).willReturn(Optional.of(WRAPPED_DOCUMENT_DATA_MODEL));
        given(conversionService.convert(WRAPPED_DOCUMENT_DATA_MODEL, StaticPageContent.class)).willReturn(STATIC_PAGE_CONTENT);

        // when
        StaticPageContent result = staticPageContentFacade.getStaticPage(StaticPageType.INTRODUCTION);

        // then
        assertThat(result, notNullValue());
    }

    @Test
    public void shouldGetStaticPageThrowContentNotFoundExceptionForMissingMapping() {

        // when
        Assertions.assertThrows(ContentNotFoundException.class, () -> staticPageContentFacade.getStaticPage(StaticPageType.CONTACT));

        // then
        // exception expected
    }

    @Test
    public void shouldGetStaticPageThrowContentNotFoundExceptionForMissingData() {

        // given
        given(contentRequestAdapterRegistry.<WrapperBodyDataModel<DocumentDataModel>, String>getContentRequestAdapter(ContentRequestAdapterIdentifier.STATIC_PAGE))
                .willReturn(staticPageContentRequestAdapter);
        given(staticPageContentRequestAdapter.getContent(INTRODUCTION_PAGE_LINK)).willReturn(Optional.empty());

        // when
        Assertions.assertThrows(ContentNotFoundException.class, () -> staticPageContentFacade.getStaticPage(StaticPageType.INTRODUCTION));

        // then
        // exception expected
    }
}
