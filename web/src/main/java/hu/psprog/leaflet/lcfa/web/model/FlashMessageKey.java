package hu.psprog.leaflet.lcfa.web.model;

/**
 * Flash message keys.
 *
 * @author Peter Smith
 */
public enum FlashMessageKey {

    SUCCESSFUL_CONTACT_REQUEST("flash.contact.success"),
    SUCCESSFUL_PASSWORD_RESET_DEMAND("flash.pwreset.demand.success"),
    SUCCESSFUL_PASSWORD_RESET_CONFIRMATION("flash.pwreset.confirm.success"),
    SUCCESSFUL_SIGN_UP("flash.signup.success"),
    SUCCESSFUL_SIGN_IN("flash.signin.success"),
    SUCCESSFUL_SIGN_OUT("flash.signout.success"),
    SUCCESSFUL_PROFILE_UPDATE("flash.profile.update.success"),
    SUCCESSFUL_ACCOUNT_DELETION("flash.profile.delete.success"),
    SUCCESSFUL_COMMENT_REQUEST("flash.comment.success"),
    SUCCESSFUL_COMMENT_DELETION("flash.comment.delete.success"),
    FAILED_PASSWORD_RESET_DEMAND("flash.pwreset.demand.failure"),
    FAILED_PASSWORD_RESET_CONFIRMATION("flash.pwreset.confirm.failure"),
    FAILED_SIGN_IN("flash.signin.failure"),
    FAILED_SIGN_UP_ADDRESS_ALREADY_IN_USE("flash.signup.failure.address"),
    FAILED_SIGN_UP_UNKNOWN_ERROR("flash.signup.failure.unknown"),
    FAILED_PROFILE_UPDATE("flash.profile.update.failure"),
    FAILED_ACCOUNT_DELETION("flash.profile.delete.failure"),
    FAILED_COMMENT_REQUEST("flash.comment.failure"),
    FAILED_COMMENT_DELETION("flash.comment.delete.failure");

    private String messageKey;

    FlashMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getMessageKey() {
        return messageKey;
    }
}
