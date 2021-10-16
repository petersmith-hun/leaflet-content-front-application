package hu.psprog.leaflet.lcfa.web.controller;

import hu.psprog.leaflet.lcfa.core.domain.common.SEOAttributes;
import hu.psprog.leaflet.lcfa.core.domain.content.ContactPageContent;
import hu.psprog.leaflet.lcfa.core.domain.content.StaticPage;
import hu.psprog.leaflet.lcfa.core.domain.content.StaticPageContent;
import hu.psprog.leaflet.lcfa.core.domain.request.ContactRequestModel;
import hu.psprog.leaflet.lcfa.core.facade.ContactPageFacade;
import hu.psprog.leaflet.lcfa.web.model.FlashMessageKey;
import hu.psprog.leaflet.lcfa.web.model.ModelField;
import hu.psprog.leaflet.lcfa.web.model.NavigationItem;
import hu.psprog.leaflet.lcfa.web.ui.support.navigation.NavigationItemFactory;
import hu.psprog.leaflet.lcfa.web.ui.support.navigation.impl.NavigationItemFactoryRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link ContactController}.
 *
 * @author Peter Smith
 */
@Extensions({
        @ExtendWith(MockitoExtension.class),
        @ExtendWith(SpringExtension.class)
})
public class ContactControllerTest extends AbstractControllerTest {

    private static final SEOAttributes SEO_ATTRIBUTES = SEOAttributes.builder().pageTitle("contact-page-seo-title").build();
    private static final StaticPage STATIC_PAGE = new StaticPage("contact-page-title", "contact-page-content");
    private static final StaticPageContent STATIC_PAGE_CONTENT = StaticPageContent.builder().page(STATIC_PAGE).seo(SEO_ATTRIBUTES).build();
    private static final ContactPageContent CONTACT_PAGE_CONTENT = ContactPageContent.builder().contactInfo(STATIC_PAGE_CONTENT).build();
    private static final NavigationItem NAVIGATION_ITEM = NavigationItem.build("link", "title");

    private static final ContactRequestModel CONTACT_REQUEST_MODEL = new ContactRequestModel();

    private static final String VIEW_GROUP_CONTACT = "contact";
    private static final String VIEW_FORM = "form";

    static {
        CONTACT_REQUEST_MODEL.setMessage("message");
    }

    @Mock
    private ContactPageFacade contactPageFacade;

    @Mock
    private NavigationItemFactoryRegistry navigationItemFactoryRegistry;

    @Mock
    private NavigationItemFactory<String> stringNavigationItemFactory;

    @InjectMocks
    private ContactController contactController;

    @Test
    public void shouldRenderContactForm() {

        // given
        givenContactForm();

        // when
        contactController.renderContactForm(CONTACT_REQUEST_MODEL);

        // then
        verifyContactForm();
    }

    @Test
    public void shouldProcessContactFormWithSuccess() {

        // when
        contactController.processContactForm(CONTACT_REQUEST_MODEL, bindingResult, redirectAttributes);

        // then
        verifyRedirectionCreated(BaseController.PATH_CONTACT);
        verifyFlashMessageSet(FlashMessageKey.SUCCESSFUL_CONTACT_REQUEST);
    }

    @Test
    public void shouldProcessContactFormWithValidationError() {

        // given
        given(bindingResult.hasErrors()).willReturn(true);
        givenContactForm();

        // when
        contactController.processContactForm(CONTACT_REQUEST_MODEL, bindingResult, redirectAttributes);

        // then
        verifyContactForm();
    }

    @Override
    String controllerViewGroup() {
        return VIEW_GROUP_CONTACT;
    }

    private void givenContactForm() {
        given(contactPageFacade.getContactPageContent()).willReturn(CONTACT_PAGE_CONTENT);
        given(navigationItemFactoryRegistry.getFactory(String.class)).willReturn(stringNavigationItemFactory);
        given(stringNavigationItemFactory.create(STATIC_PAGE.getTitle())).willReturn(NAVIGATION_ITEM);
    }

    private void verifyContactForm() {
        verifyViewCreated(VIEW_FORM);
        verifyFieldSet(ModelField.STATIC, STATIC_PAGE);
        verifyFieldSet(ModelField.VALIDATED_MODEL, CONTACT_REQUEST_MODEL);
        verifyFieldSet(ModelField.NAVIGATION, NAVIGATION_ITEM);
        verifySeoOverride(SEO_ATTRIBUTES);
    }
}
