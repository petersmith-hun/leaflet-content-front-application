package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.document.DocumentDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.service.DocumentBridgeService;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

/**
 * Unit tests for {@link StaticPageContentRequestAdapter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class StaticPageContentRequestAdapterTest {

    private static final String DOCUMENT_LINK = "document-link";
    private static final WrapperBodyDataModel<DocumentDataModel> WRAPPED_DOCUMENT_DATA_MODEL = WrapperBodyDataModel.getBuilder()
            .withBody(DocumentDataModel.getBuilder().withLink(DOCUMENT_LINK).build())
            .build();

    @Mock
    private DocumentBridgeService documentBridgeService;

    @InjectMocks
    private StaticPageContentRequestAdapter adapter;

    @Test
    public void shouldGetContentReturnWithSuccess() throws CommunicationFailureException {

        // given
        given(documentBridgeService.getDocumentByLink(DOCUMENT_LINK)).willReturn(WRAPPED_DOCUMENT_DATA_MODEL);

        // when
        Optional<WrapperBodyDataModel<DocumentDataModel>> result = adapter.getContent(DOCUMENT_LINK);

        // then
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), equalTo(WRAPPED_DOCUMENT_DATA_MODEL));
    }

    @Test
    public void shouldGetContentReturnWithEmptyResponseInCaseOfBridgeFailure() throws CommunicationFailureException {

        // given
        doThrow(CommunicationFailureException.class).when(documentBridgeService).getDocumentByLink(DOCUMENT_LINK);

        // when
        Optional<WrapperBodyDataModel<DocumentDataModel>> result = adapter.getContent(DOCUMENT_LINK);

        // then
        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void shouldGetIdentifierReturnProperValue() {

        // when
        ContentRequestAdapterIdentifier result = adapter.getIdentifier();

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(ContentRequestAdapterIdentifier.STATIC_PAGE));
    }
}
