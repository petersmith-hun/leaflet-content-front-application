package hu.psprog.leaflet.lcfa.core.domain.request;

import hu.psprog.leaflet.api.rest.request.user.UserPasswordRequestModel;
import hu.psprog.leaflet.api.rest.request.validator.PasswordConfirmCheck;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * Sign-up request model.
 * Includes ReCaptcha token field.
 *
 * @author Peter Smith
 */
@Data
@PasswordConfirmCheck
@EqualsAndHashCode(callSuper = true)
public class SignUpRequestModel extends UserPasswordRequestModel {

    @NotEmpty
    @Size(max = 255)
    private String username;

    @Email
    @NotEmpty
    @Size(max = 255)
    private String email;

    @NotEmpty
    private String recaptchaToken;
}
