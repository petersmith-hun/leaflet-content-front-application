package hu.psprog.leaflet.lcfa.core.domain.request;

import lombok.Data;

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
public class SignUpRequestModel {

    @NotEmpty
    @Size(max = 255)
    private String username;

    @Email
    @NotEmpty
    @Size(max = 255)
    private String email;

    @NotEmpty
    private String password;

    @NotEmpty
    private String passwordConfirmation;

    @NotEmpty
    private String recaptchaToken;
}
