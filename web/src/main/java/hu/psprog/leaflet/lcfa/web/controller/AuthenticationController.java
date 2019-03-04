package hu.psprog.leaflet.lcfa.web.controller;

import hu.psprog.leaflet.lcfa.core.domain.request.PasswordReclaimRequestModel;
import hu.psprog.leaflet.lcfa.core.domain.request.SignUpRequestModel;
import hu.psprog.leaflet.lcfa.web.factory.ModelAndViewFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller implementation for authentication related operations.
 *
 * @author Peter Smith
 */
@Controller
public class AuthenticationController {

    private static final String VIEW_USERS_SIGN_IN = "view/users/sign_in";
    private static final String VIEW_USERS_SIGN_UP = "view/users/sign_up";

    private static final String PATH_SIGN_IN = "/signin";
    private static final String PATH_SIGN_UP = "/signup";

    private ModelAndViewFactory modelAndViewFactory;

    @Autowired
    public AuthenticationController(ModelAndViewFactory modelAndViewFactory) {
        this.modelAndViewFactory = modelAndViewFactory;
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
                .build();
    }

    /**
     * TODO.
     *
     * @param signUpRequestModel
     * @return
     */
    @PostMapping
    public ModelAndView processSignUpRequest(@ModelAttribute SignUpRequestModel signUpRequestModel) {

        return modelAndViewFactory.createRedirectionTo(PATH_SIGN_IN);
    }
}
