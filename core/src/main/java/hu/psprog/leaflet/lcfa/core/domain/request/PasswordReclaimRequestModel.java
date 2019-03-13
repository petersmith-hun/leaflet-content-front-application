package hu.psprog.leaflet.lcfa.core.domain.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * Password reclaim request model.
 * Includes ReCaptcha token field.
 *
 * @author Peter Smith
 */
@Data
public class PasswordReclaimRequestModel {

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String recaptchaToken;
}
