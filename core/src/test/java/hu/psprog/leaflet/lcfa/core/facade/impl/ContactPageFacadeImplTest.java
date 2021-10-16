package hu.psprog.leaflet.lcfa.core.facade.impl;

import hu.psprog.leaflet.lcfa.core.domain.content.ContactPageContent;
import hu.psprog.leaflet.lcfa.core.domain.content.StaticPageContent;
import hu.psprog.leaflet.lcfa.core.domain.content.StaticPageType;
import hu.psprog.leaflet.lcfa.core.domain.request.ContactRequestModel;
import hu.psprog.leaflet.lcfa.core.exception.UserRequestProcessingException;
import hu.psprog.leaflet.lcfa.core.facade.StaticPageContentFacade;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapter;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterRegistry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link ContactPageFacadeImpl}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class ContactPageFacadeImplTest {

    private static final StaticPageContent STATIC_PAGE_CONTENT = StaticPageContent.builder().build();
    private static final ContactPageContent CONTACT_PAGE_CONTENT = ContactPageContent.builder()
            .contactInfo(STATIC_PAGE_CONTENT)
            .build();
    private static final ContactRequestModel CONTACT_REQUEST_MODEL = new ContactRequestModel();

    static {
        CONTACT_REQUEST_MODEL.setMessage("message");
    }

    @Mock
    private ContentRequestAdapterRegistry contentRequestAdapterRegistry;

    @Mock
    private StaticPageContentFacade staticPageContentFacade;

    @Mock
    private ContentRequestAdapter<Boolean, ContactRequestModel> contactRequestContentRequestAdapter;

    @InjectMocks
    private ContactPageFacadeImpl contactPageFacade;

    @Test
    public void shouldGetContactPageContentWithSuccess() {

        // given
        given(staticPageContentFacade.getStaticPage(StaticPageType.CONTACT)).willReturn(STATIC_PAGE_CONTENT);

        // when
        ContactPageContent result = contactPageFacade.getContactPageContent();

        // then
        assertThat(result, equalTo(CONTACT_PAGE_CONTENT));
    }

    @Test
    public void shouldProcessContactRequestWithSuccess() {

        // given
        given(contentRequestAdapterRegistry.<Boolean, ContactRequestModel>getContentRequestAdapter(ContentRequestAdapterIdentifier.CONTACT_REQUEST))
                .willReturn(contactRequestContentRequestAdapter);
        given(contactRequestContentRequestAdapter.getContent(CONTACT_REQUEST_MODEL)).willReturn(Optional.of(true));

        // when
        contactPageFacade.processContactRequest(CONTACT_REQUEST_MODEL);

        // then
        verify(contactRequestContentRequestAdapter).getContent(CONTACT_REQUEST_MODEL);
    }

    @Test
    public void shouldProcessContactRequestThrowUserRequestProcessingExceptionForMissingResponseData() {

        // given
        given(contentRequestAdapterRegistry.<Boolean, ContactRequestModel>getContentRequestAdapter(ContentRequestAdapterIdentifier.CONTACT_REQUEST))
                .willReturn(contactRequestContentRequestAdapter);
        given(contactRequestContentRequestAdapter.getContent(CONTACT_REQUEST_MODEL)).willReturn(Optional.empty());

        // when
        Assertions.assertThrows(UserRequestProcessingException.class, () -> contactPageFacade.processContactRequest(CONTACT_REQUEST_MODEL));

        // then
        // exception expected
    }
}
