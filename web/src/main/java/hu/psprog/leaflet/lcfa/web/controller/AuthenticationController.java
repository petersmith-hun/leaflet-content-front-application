package hu.psprog.leaflet.lcfa.web.controller;

import hu.psprog.leaflet.lcfa.core.domain.request.PasswordReclaimRequestModel;
import hu.psprog.leaflet.lcfa.core.domain.request.SignUpRequestModel;
import hu.psprog.leaflet.lcfa.core.domain.result.SignUpResult;
import hu.psprog.leaflet.lcfa.core.facade.AuthenticationFacade;
import hu.psprog.leaflet.lcfa.web.factory.ModelAndViewFactory;
import hu.psprog.leaflet.lcfa.web.model.FlashMessageKey;
import hu.psprog.leaflet.lcfa.web.model.ModelField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

    private static final String PATH_SIGN_IN = "/signin";
    private static final String PATH_SIGN_UP = "/signup";

    private static final Map<SignUpResult, FlashMessageKey> SIGN_UP_RESULT_FLASH_MESSAGE_KEY_MAP = Map.of(
            SignUpResult.SUCCESS, FlashMessageKey.SUCCESSFUL_SIGN_UP,
            SignUpResult.ADDRESS_IN_USE, FlashMessageKey.FAILED_SIGN_UP_ADDRESS_ALREADY_IN_USE,
            SignUpResult.FAILURE, FlashMessageKey.FAILED_SIGN_UP_UNKNOWN_ERROR);

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

    private ModelAndView handleSignUpResult(SignUpResult signUpResult, RedirectAttributes redirectAttributes) {

        String redirectionPath = mapResultToRedirectionPath(signUpResult);
        FlashMessageKey flashMessageKey = mapResultToFlashMessageKey(signUpResult);
        flash(redirectAttributes, flashMessageKey);

        return modelAndViewFactory.createRedirectionTo(redirectionPath);
    }

    private String mapResultToRedirectionPath(SignUpResult signUpResult) {

        return SignUpResult.SUCCESS == signUpResult
                ? PATH_SIGN_IN
                : PATH_SIGN_UP;
    }

    private FlashMessageKey mapResultToFlashMessageKey(SignUpResult signUpResult) {
        return SIGN_UP_RESULT_FLASH_MESSAGE_KEY_MAP.getOrDefault(signUpResult, FlashMessageKey.FAILED_SIGN_UP_UNKNOWN_ERROR);
    }
}
