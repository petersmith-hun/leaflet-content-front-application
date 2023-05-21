package hu.psprog.leaflet.lcfa.core.facade.impl;

import hu.psprog.leaflet.lcfa.core.domain.content.ContactPageContent;
import hu.psprog.leaflet.lcfa.core.domain.content.StaticPageType;
import hu.psprog.leaflet.lcfa.core.domain.request.ContactRequestModel;
import hu.psprog.leaflet.lcfa.core.exception.UserRequestProcessingException;
import hu.psprog.leaflet.lcfa.core.facade.ContactPageFacade;
import hu.psprog.leaflet.lcfa.core.facade.StaticPageContentFacade;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier.CONTACT_REQUEST;

/**
 * Implementation of {@link ContactPageFacade}.
 *
 * @author Peter Smith
 */
@Service
public class ContactPageFacadeImpl implements ContactPageFacade {

    private final ContentRequestAdapterRegistry contentRequestAdapterRegistry;
    private final StaticPageContentFacade staticPageContentFacade;

    @Autowired
    public ContactPageFacadeImpl(ContentRequestAdapterRegistry contentRequestAdapterRegistry, StaticPageContentFacade staticPageContentFacade) {
        this.contentRequestAdapterRegistry = contentRequestAdapterRegistry;
        this.staticPageContentFacade = staticPageContentFacade;
    }

    @Override
    public ContactPageContent getContactPageContent() {
        return ContactPageContent.builder()
                .contactInfo(staticPageContentFacade.getStaticPage(StaticPageType.CONTACT))
                .build();
    }

    @Override
    public void processContactRequest(ContactRequestModel contactRequestModel) {
        contentRequestAdapterRegistry.<Boolean, ContactRequestModel>getContentRequestAdapter(CONTACT_REQUEST)
                .getContent(contactRequestModel)
                .orElseThrow(() -> new UserRequestProcessingException("Contact request could not be processed"));
    }
}
