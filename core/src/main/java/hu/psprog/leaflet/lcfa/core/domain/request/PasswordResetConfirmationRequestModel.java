package hu.psprog.leaflet.lcfa.core.domain.request;

import hu.psprog.leaflet.api.rest.request.user.UserPasswordRequestModel;
import hu.psprog.leaflet.api.rest.request.validator.PasswordConfirmCheck;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;

/**
 * Password reset confirmation request model.
 * Includes ReCaptcha token field.
 *
 * @author Peter Smith
 */
@Data
@PasswordConfirmCheck
@EqualsAndHashCode(callSuper = true)
public class PasswordResetConfirmationRequestModel extends UserPasswordRequestModel {

    @NotEmpty
    private String recaptchaToken;
}
