package hu.psprog.leaflet.lcfa.web.model;

/**
 * Flash message keys.
 *
 * @author Peter Smith
 */
public enum FlashMessageKey {

    SUCCESSFUL_CONTACT_REQUEST("flash.contact.success");

    private String messageKey;

    FlashMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getMessageKey() {
        return messageKey;
    }
}
