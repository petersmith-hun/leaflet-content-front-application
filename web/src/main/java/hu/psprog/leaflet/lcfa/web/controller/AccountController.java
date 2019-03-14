package hu.psprog.leaflet.lcfa.web.controller;

import hu.psprog.leaflet.api.rest.request.user.PasswordChangeRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UpdateProfileRequestModel;
import hu.psprog.leaflet.lcfa.web.factory.ModelAndViewFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller implementation for account related operations.
 *
 * @author Peter Smith
 */
@Controller
@RequestMapping(AccountController.PATH_PROFILE)
public class AccountController extends BaseController {

    private static final String VIEW_ACCOUNT_PROFILE = "view/account/profile";
    private static final String VIEW_ACCOUNT_PASSWORD_CHANGE = "view/account/pw_change";
    private static final String VIEW_ACCOUNT_COMMENTS = "view/account/comments";
    private static final String VIEW_ACCOUNT_DELETE = "view/account/delete";

    private static final String PATH_CHANGE_PASSWORD = "/change-password";
    private static final String PATH_MY_COMMENTS = "/my-comments";
    private static final String PATH_DELETE_ACCOUNT = "/delete-account";
    static final String PATH_PROFILE = "/profile";

    private ModelAndViewFactory modelAndViewFactory;


    @Autowired
    public AccountController(ModelAndViewFactory modelAndViewFactory) {
        this.modelAndViewFactory = modelAndViewFactory;
    }

    @GetMapping
    public ModelAndView renderProfileForm(@ModelAttribute UpdateProfileRequestModel updateProfileRequestModel) {

        return modelAndViewFactory.createForView(VIEW_ACCOUNT_PROFILE)
                .build();
    }

    @GetMapping(PATH_CHANGE_PASSWORD)
    public ModelAndView renderPasswordChangeForm(@ModelAttribute PasswordChangeRequestModel passwordChangeRequestModel) {

        return modelAndViewFactory.createForView(VIEW_ACCOUNT_PASSWORD_CHANGE)
                .build();
    }

    @GetMapping(PATH_MY_COMMENTS)
    public ModelAndView renderComments() {

        return modelAndViewFactory.createForView(VIEW_ACCOUNT_COMMENTS)
                .build();
    }

    @GetMapping(PATH_DELETE_ACCOUNT)
    public ModelAndView renderAccountDeletionForm() {

        return modelAndViewFactory.createForView(VIEW_ACCOUNT_DELETE)
                .build();
    }
}
