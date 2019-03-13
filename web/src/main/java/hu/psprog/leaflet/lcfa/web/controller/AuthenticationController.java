package hu.psprog.leaflet.lcfa.web.controller;

import hu.psprog.leaflet.lcfa.core.domain.request.PasswordReclaimRequestModel;
import hu.psprog.leaflet.lcfa.core.domain.request.PasswordResetConfirmationRequestModel;
import hu.psprog.leaflet.lcfa.core.domain.request.SignUpRequestModel;
import hu.psprog.leaflet.lcfa.core.domain.result.PasswordResetResult;
import hu.psprog.leaflet.lcfa.core.domain.result.SignUpResult;
import hu.psprog.leaflet.lcfa.core.facade.AuthenticationFacade;
import hu.psprog.leaflet.lcfa.web.factory.ModelAndViewFactory;
import hu.psprog.leaflet.lcfa.web.model.FlashMessageKey;
import hu.psprog.leaflet.lcfa.web.model.ModelField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Map;

/**
 * Controller implementation for authentication related operations.
 *
 * @author Peter Smith
 */
@Controller
public class AuthenticationController extends BaseController {

    private static final String VIEW_USERS_SIGN_IN = "view/users/sign_in";
    private static final String VIEW_USERS_SIGN_UP = "view/users/sign_up";
    private static final String VIEW_USERS_PW_RESET = "view/users/pw_reset";

    private static final String PATH_HOME = "/";
    private static final String PATH_SIGN_IN = "/signin";
    private static final String PATH_SIGN_UP = "/signup";
    private static final String PATH_PASSWORD_RESET_CONFIRMATION = "/password-reset/{token:.+}";
    private static final String PATH_PASSWORD_RESET_REQUEST = "/password-reset";

    private static final Map<SignUpResult, FlashMessageKey> SIGN_UP_RESULT_FLASH_MESSAGE_KEY_MAP = Map.of(
            SignUpResult.SUCCESS, FlashMessageKey.SUCCESSFUL_SIGN_UP,
            SignUpResult.ADDRESS_IN_USE, FlashMessageKey.FAILED_SIGN_UP_ADDRESS_ALREADY_IN_USE,
            SignUpResult.FAILURE, FlashMessageKey.FAILED_SIGN_UP_UNKNOWN_ERROR);

    private static final Map<PasswordResetResult, FlashMessageKey> PASSWORD_RESET_RESULT_FLASH_MESSAGE_KEY_MAP = Map.of(
            PasswordResetResult.DEMAND_PROCESSED, FlashMessageKey.SUCCESSFUL_PASSWORD_RESET_DEMAND,
            PasswordResetResult.DEMAND_FAILED, FlashMessageKey.FAILED_PASSWORD_RESET_DEMAND,
            PasswordResetResult.CONFIRMATION_PROCESSED, FlashMessageKey.SUCCESSFUL_PASSWORD_RESET_CONFIRMATION,
            PasswordResetResult.CONFIRMATION_FAILED, FlashMessageKey.FAILED_PASSWORD_RESET_CONFIRMATION);

    private ModelAndViewFactory modelAndViewFactory;
    private AuthenticationFacade authenticationFacade;

    @Autowired
    public AuthenticationController(ModelAndViewFactory modelAndViewFactory, AuthenticationFacade authenticationFacade) {
        this.modelAndViewFactory = modelAndViewFactory;
        this.authenticationFacade = authenticationFacade;
    }

    /**
     * GET /signin
     * Renders sign-in form.
     *
     * @param passwordReclaimRequestModel {@link PasswordReclaimRequestModel} object in order to re-populate the form after a validation error
     * @return populated {@link ModelAndView} object
     */
    @GetMapping(PATH_SIGN_IN)
    public ModelAndView renderSignInForm(@ModelAttribute PasswordReclaimRequestModel passwordReclaimRequestModel) {

        return modelAndViewFactory.createForView(VIEW_USERS_SIGN_IN)
                .build();
    }

    /**
     * GET /signup
     * Renders sign-up form.
     *
     * @param signUpRequestModel {@link SignUpRequestModel} object in order to re-populate the form after a validation error
     * @return populated {@link ModelAndView} object
     */
    @GetMapping(PATH_SIGN_UP)
    public ModelAndView renderSignUpForm(@ModelAttribute SignUpRequestModel signUpRequestModel) {

        return modelAndViewFactory.createForView(VIEW_USERS_SIGN_UP)
                .withAttribute(ModelField.VALIDATED_MODEL, signUpRequestModel)
                .build();
    }

    /**
     * POST /signup
     * Processes the given sign-up request. Renders sign-up form with current {@link SignUpRequestModel} in case of a validation error.
     *
     * @param signUpRequestModel form contents as {@link SignUpRequestModel}
     * @param bindingResult validation results
     * @param redirectAttributes redirection attributes for passing flash message
     * @return populated {@link ModelAndView} object
     */
    @PostMapping(PATH_SIGN_UP)
    public ModelAndView processSignUpRequest(@ModelAttribute @Valid SignUpRequestModel signUpRequestModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        ModelAndView modelAndView;
        if (bindingResult.hasErrors()) {
            modelAndView = renderSignUpForm(signUpRequestModel);
        } else {
            SignUpResult signUpResult = authenticationFacade.signUp(signUpRequestModel);
            modelAndView = handleSignUpResult(signUpResult, redirectAttributes);
        }

        return modelAndView;
    }

    /**
     * POST /password-reset
     * Processed a password reset demand.
     * Redirects to home after successfully processing the request. On failure, redirects to self.
     *
     * @param passwordReclaimRequestModel form contents as {@link PasswordReclaimRequestModel}
     * @param bindingResult validation results
     * @param redirectAttributes redirection attributes for passing flash message
     * @return populated {@link ModelAndView} object
     */
    @PostMapping(PATH_PASSWORD_RESET_REQUEST)
    public ModelAndView processPasswordResetRequest(@ModelAttribute @Valid PasswordReclaimRequestModel passwordReclaimRequestModel,
                                                    BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        ModelAndView modelAndView;
        if (bindingResult.hasErrors()) {
            modelAndView = renderSignInForm(passwordReclaimRequestModel);
        } else {
            PasswordResetResult result = authenticationFacade.requestPasswordReset(passwordReclaimRequestModel);
            modelAndView = handlePasswordResetResult(result, redirectAttributes);
        }

        return modelAndView;
    }

    /**
     * GET /password-reset/{token}
     * Renders password reset form.
     *
     * @param passwordResetToken password reset reclaim token sent in the reset email by the backend (unused here, but needed by the processor endpoint)
     * @param passwordResetConfirmationRequestModel {@link PasswordResetConfirmationRequestModel} object in order to re-populate the form after a validation error
     * @return populated {@link ModelAndView} object
     */
    @GetMapping(PATH_PASSWORD_RESET_CONFIRMATION)
    public ModelAndView renderPasswordResetConfirmationForm(@PathVariable("token") String passwordResetToken,
                                                            @ModelAttribute PasswordResetConfirmationRequestModel passwordResetConfirmationRequestModel) {

        return modelAndViewFactory.createForView(VIEW_USERS_PW_RESET)
                .build();
    }

    /**
     * POST /password-reset/{token}
     * Processes a password reset confirmation request.
     * Redirects to sign-in endpoint after successfully processing the request and on failure as well.
     *
     * @param passwordResetToken password reset reclaim token sent in the reset email by the backend
     * @param passwordResetConfirmationRequestModel form contents as {@link PasswordResetConfirmationRequestModel}
     * @param bindingResult validation results
     * @param redirectAttributes redirection attributes for passing flash message
     * @return populated {@link ModelAndView} object
     */
    @PostMapping(PATH_PASSWORD_RESET_CONFIRMATION)
    public ModelAndView processPasswordResetConfirmationForm(@PathVariable("token") String passwordResetToken,
                                                             @ModelAttribute @Valid PasswordResetConfirmationRequestModel passwordResetConfirmationRequestModel,
                                                             BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        ModelAndView modelAndView;
        if (bindingResult.hasErrors()) {
            modelAndView = renderPasswordResetConfirmationForm(passwordResetToken, passwordResetConfirmationRequestModel);
        } else {
            try {
                PasswordResetResult result = authenticationFacade.confirmPasswordReset(passwordResetConfirmationRequestModel, passwordResetToken);
                modelAndView = handlePasswordResetResult(result, redirectAttributes);
            } finally {
                SecurityContextHolder.clearContext();
            }
        }

        return modelAndView;
    }

    private ModelAndView handleSignUpResult(SignUpResult signUpResult, RedirectAttributes redirectAttributes) {

        String redirectionPath = mapResultToRedirectionPath(signUpResult);
        FlashMessageKey flashMessageKey = mapResultToFlashMessageKey(signUpResult);
        flash(redirectAttributes, flashMessageKey);

        return modelAndViewFactory.createRedirectionTo(redirectionPath);
    }

    private ModelAndView handlePasswordResetResult(PasswordResetResult passwordResetResult, RedirectAttributes redirectAttributes) {

        String redirectionPath = mapResultToRedirectionPath(passwordResetResult);
        FlashMessageKey flashMessageKey = mapResultToFlashMessageKey(passwordResetResult);
        flash(redirectAttributes, flashMessageKey);

        return modelAndViewFactory.createRedirectionTo(redirectionPath);
    }

    private String mapResultToRedirectionPath(SignUpResult signUpResult) {

        return SignUpResult.SUCCESS == signUpResult
                ? PATH_SIGN_IN
                : PATH_SIGN_UP;
    }

    private String mapResultToRedirectionPath(PasswordResetResult passwordResetResult) {

        return PasswordResetResult.DEMAND_PROCESSED == passwordResetResult
                ? PATH_HOME
                : PATH_SIGN_IN;
    }

    private FlashMessageKey mapResultToFlashMessageKey(SignUpResult signUpResult) {
        return SIGN_UP_RESULT_FLASH_MESSAGE_KEY_MAP.getOrDefault(signUpResult, FlashMessageKey.FAILED_SIGN_UP_UNKNOWN_ERROR);
    }

    private FlashMessageKey mapResultToFlashMessageKey(PasswordResetResult passwordResetResult) {
        return PASSWORD_RESET_RESULT_FLASH_MESSAGE_KEY_MAP.get(passwordResetResult);
    }
}
