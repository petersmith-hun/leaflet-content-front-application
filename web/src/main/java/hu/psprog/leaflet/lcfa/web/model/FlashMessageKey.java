package hu.psprog.leaflet.lcfa.web.model;

/**
 * Flash message keys.
 *
 * @author Peter Smith
 */
public enum FlashMessageKey {

    SUCCESSFUL_CONTACT_REQUEST("flash.contact.success"),
    SUCCESSFUL_SIGN_UP("flash.signup.success"),
    SUCCESSFUL_SIGN_IN("flash.signin.success"),
    SUCCESSFUL_SIGN_OUT("flash.signout.success"),
    FAILED_SIGN_IN("flash.signin.failure"),
    FAILED_SIGN_UP_ADDRESS_ALREADY_IN_USE("flash.signup.failure.address"),
    FAILED_SIGN_UP_UNKNOWN_ERROR("flash.signup.failure.unknown");

    private String messageKey;

    FlashMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getMessageKey() {
        return messageKey;
    }
}
