package hu.psprog.leaflet.lcfa.web.controller;

import hu.psprog.leaflet.lcfa.core.domain.common.CommonPageDataField;
import hu.psprog.leaflet.lcfa.core.domain.content.ContactPageContent;
import hu.psprog.leaflet.lcfa.core.domain.request.ContactRequestModel;
import hu.psprog.leaflet.lcfa.core.facade.ContactPageFacade;
import hu.psprog.leaflet.lcfa.web.factory.ModelAndViewFactory;
import hu.psprog.leaflet.lcfa.web.model.FlashMessageKey;
import hu.psprog.leaflet.lcfa.web.model.ModelField;
import hu.psprog.leaflet.lcfa.web.ui.support.navigation.impl.NavigationItemFactoryRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

/**
 * Controller implementation for contact handling requests.
 *
 * @author Peter Smith
 */
@Controller
@RequestMapping(BaseController.PATH_CONTACT)
public class ContactController extends BaseController {

    private static final String VIEW_CONTACT_FORM = "view/contact/form";

    private final ModelAndViewFactory modelAndViewFactory;
    private final ContactPageFacade contactPageFacade;
    private final NavigationItemFactoryRegistry navigationItemFactoryRegistry;

    @Autowired
    public ContactController(ModelAndViewFactory modelAndViewFactory, ContactPageFacade contactPageFacade,
                             NavigationItemFactoryRegistry navigationItemFactoryRegistry) {
        this.modelAndViewFactory = modelAndViewFactory;
        this.contactPageFacade = contactPageFacade;
        this.navigationItemFactoryRegistry = navigationItemFactoryRegistry;
    }

    /**
     * GET /contact
     * Renders contact page.
     *
     * @param contactRequestModel {@link ContactRequestModel} object in order to re-populate the form after a validation error
     * @return populated {@link ModelAndView} object
     */
    @GetMapping
    public ModelAndView renderContactForm(@ModelAttribute ContactRequestModel contactRequestModel) {
        ContactPageContent contactPageContent = contactPageFacade.getContactPageContent();
        return modelAndViewFactory.createForView(VIEW_CONTACT_FORM)
                .withAttribute(ModelField.STATIC, contactPageContent.contactInfo().page())
                .withAttribute(CommonPageDataField.SEO_ATTRIBUTES.getFieldName(), contactPageContent.contactInfo().seo())
                .withAttribute(ModelField.VALIDATED_MODEL, contactRequestModel)
                .withAttribute(ModelField.NAVIGATION, navigationItemFactoryRegistry
                        .getFactory(String.class)
                        .create(contactPageContent.contactInfo().page().title()))
                .build();
    }

    /**
     * POST /contact
     * Processes contact request. Renders contact form with current {@link ContactRequestModel} in case of a validation error.
     *
     * @param contactRequestModel form contents as {@link ContactRequestModel}
     * @param bindingResult validation results
     * @param redirectAttributes redirection attributes for passing flash message
     * @return populated {@link ModelAndView} object
     */
    @PostMapping
    public ModelAndView processContactForm(@ModelAttribute @Valid ContactRequestModel contactRequestModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        ModelAndView modelAndView;
        if (bindingResult.hasErrors()) {
            modelAndView = renderContactForm(contactRequestModel);
        } else {
            contactPageFacade.processContactRequest(contactRequestModel);
            modelAndView = modelAndViewFactory.createRedirectionTo(PATH_CONTACT);
            flash(redirectAttributes, FlashMessageKey.SUCCESSFUL_CONTACT_REQUEST);
        }

        return modelAndView;
    }
}
