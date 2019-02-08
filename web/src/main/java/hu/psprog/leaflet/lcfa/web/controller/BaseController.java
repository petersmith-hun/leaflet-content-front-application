package hu.psprog.leaflet.lcfa.web.controller;

import hu.psprog.leaflet.lcfa.web.model.FlashMessageKey;
import hu.psprog.leaflet.lcfa.web.model.ModelField;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Base controller providing common methods.
 *
 * @author Peter Smith
 */
abstract class BaseController {

    /**
     * Sets the given flash message in the redirect attributes.
     *
     * @param redirectAttributes {@link RedirectAttributes} object for setting flash messages
     * @param flashMessageKey {@link FlashMessageKey} enum constant containing an actual message key (needs to be translated)
     */
    void flash(RedirectAttributes redirectAttributes, FlashMessageKey flashMessageKey) {
        redirectAttributes.addFlashAttribute(ModelField.FLASH.getFieldName(), flashMessageKey.getMessageKey());
    }
}
