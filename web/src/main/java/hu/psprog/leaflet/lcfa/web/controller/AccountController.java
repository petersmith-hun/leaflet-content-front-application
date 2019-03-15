package hu.psprog.leaflet.lcfa.web.controller;

import hu.psprog.leaflet.api.rest.request.user.PasswordChangeRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UpdateProfileRequestModel;
import hu.psprog.leaflet.lcfa.core.domain.request.AccountDeletionRequest;
import hu.psprog.leaflet.lcfa.core.facade.AccountManagementFacade;
import hu.psprog.leaflet.lcfa.web.factory.ModelAndViewFactory;
import hu.psprog.leaflet.lcfa.web.model.FlashMessageKey;
import hu.psprog.leaflet.lcfa.web.model.ModelField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * Controller implementation for account related operations.
 *
 * @author Peter Smith
 */
@Controller
@RequestMapping(AccountController.PATH_PROFILE)
public class AccountController extends BaseController {

    static final String PATH_PROFILE = "/profile";

    private static final String VIEW_ACCOUNT_PROFILE = "view/account/profile";
    private static final String VIEW_ACCOUNT_PASSWORD_CHANGE = "view/account/pw_change";
    private static final String VIEW_ACCOUNT_COMMENTS = "view/account/comments";
    private static final String VIEW_ACCOUNT_DELETE = "view/account/delete";

    private static final String PATH_CHANGE_PASSWORD = "/change-password";
    private static final String PATH_MY_COMMENTS = "/my-comments";
    private static final String PATH_DELETE_ACCOUNT = "/delete-account";
    private static final String PATH_PROFILE_DELETE_ACCOUNT = PATH_PROFILE + PATH_DELETE_ACCOUNT;
    private static final String PATH_PROFILE_CHANGE_PASSWORD = PATH_PROFILE + PATH_CHANGE_PASSWORD;
    private static final String PATH_HOME = "/";

    private ModelAndViewFactory modelAndViewFactory;
    private AccountManagementFacade accountManagementFacade;

    @Autowired
    public AccountController(ModelAndViewFactory modelAndViewFactory, AccountManagementFacade accountManagementFacade) {
        this.modelAndViewFactory = modelAndViewFactory;
        this.accountManagementFacade = accountManagementFacade;
    }

    /**
     * GET /profile
     * Renders profile base info form.
     *
     * @param updateProfileRequestModel {@link UpdateProfileRequestModel} object in order to re-populate the form after a validation error
     * @return populated {@link ModelAndView} object
     */
    @GetMapping
    public ModelAndView renderProfileForm(@ModelAttribute UpdateProfileRequestModel updateProfileRequestModel) {

        return modelAndViewFactory.createForView(VIEW_ACCOUNT_PROFILE)
                .withAttribute(ModelField.ACCOUNT, accountManagementFacade.getAccountBaseInfo(currentUserID()))
                .build();
    }

    /**
     * POST /profile
     * Processes profile update request.
     *
     * @param updateProfileRequestModel {@link UpdateProfileRequestModel} object containing form data
     * @param bindingResult validation results
     * @param redirectAttributes redirection attributes
     * @return populated {@link ModelAndView} object
     */
    @PostMapping
    public ModelAndView processProfileUpdateRequest(@ModelAttribute @Valid UpdateProfileRequestModel updateProfileRequestModel,
                                                    BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        ModelAndView modelAndView;
        if (bindingResult.hasErrors()) {
            modelAndView = renderProfileForm(updateProfileRequestModel);
        } else {
            boolean successful = accountManagementFacade.updateAccountBaseInfo(currentUserID(), updateProfileRequestModel);
            flash(redirectAttributes, getFlashMessageKeyForUpdateResult(successful));
            modelAndView = modelAndViewFactory.createRedirectionTo(PATH_PROFILE);
        }

        return modelAndView;
    }

    /**
     * GET /profile/change-password
     * Renders password change form.
     *
     * @param passwordChangeRequestModel {@link PasswordChangeRequestModel} object in order to show validation errors
     * @return populated {@link ModelAndView} object
     */
    @GetMapping(PATH_CHANGE_PASSWORD)
    public ModelAndView renderPasswordChangeForm(@ModelAttribute PasswordChangeRequestModel passwordChangeRequestModel) {

        return modelAndViewFactory.createForView(VIEW_ACCOUNT_PASSWORD_CHANGE)
                .build();
    }

    /**
     * POST /profile/change-password
     * Processes a password change request.
     *
     * @param passwordChangeRequestModel {@link PasswordChangeRequestModel} object containing form data
     * @param bindingResult validation results
     * @param redirectAttributes redirection attributes
     * @return populated {@link ModelAndView} object
     */
    @PostMapping(PATH_CHANGE_PASSWORD)
    public ModelAndView processPasswordChangeRequest(@ModelAttribute @Valid PasswordChangeRequestModel passwordChangeRequestModel,
                                                     BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        ModelAndView modelAndView;
        if (bindingResult.hasErrors()) {
            modelAndView = renderPasswordChangeForm(passwordChangeRequestModel);
        } else {
            boolean successful = accountManagementFacade.updatePassword(currentUserID(), passwordChangeRequestModel);
            flash(redirectAttributes, getFlashMessageKeyForUpdateResult(successful));
            modelAndView = modelAndViewFactory.createRedirectionTo(PATH_PROFILE_CHANGE_PASSWORD);
        }

        return modelAndView;
    }

    /**
     * GET /profile/my-comments
     * Renders list of authenticated user's existing comments.
     *
     * @return populated {@link ModelAndView} object
     */
    @GetMapping(PATH_MY_COMMENTS)
    public ModelAndView renderComments() {

        return modelAndViewFactory.createForView(VIEW_ACCOUNT_COMMENTS)
                .build();
    }

    /**
     * GET /profile/delete-account
     * Renders account deletion form.
     *
     * @return populated {@link ModelAndView} object
     */
    @GetMapping(PATH_DELETE_ACCOUNT)
    public ModelAndView renderAccountDeletionForm() {

        return modelAndViewFactory.createForView(VIEW_ACCOUNT_DELETE)
                .build();
    }

    /**
     * POST /profile/delete-account
     * Processes an account deletion request.
     *
     * @param accountDeletionRequest {@link AccountDeletionRequest} object containing form data
     * @param redirectAttributes redirection attributes
     * @return populated {@link ModelAndView} object
     */
    @PostMapping(PATH_DELETE_ACCOUNT)
    public ModelAndView processAccountDeletionRequest(@ModelAttribute AccountDeletionRequest accountDeletionRequest, RedirectAttributes redirectAttributes) {

        ModelAndView modelAndView;
        FlashMessageKey flashMessageKey;
        if (accountManagementFacade.deleteAccount(currentUserID(), accountDeletionRequest)) {
            modelAndView = modelAndViewFactory.createRedirectionTo(PATH_HOME);
            flashMessageKey = FlashMessageKey.SUCCESSFUL_ACCOUNT_DELETION;
        } else {
            modelAndView = modelAndViewFactory.createRedirectionTo(PATH_PROFILE_DELETE_ACCOUNT);
            flashMessageKey = FlashMessageKey.FAILED_ACCOUNT_DELETION;
        }

        flash(redirectAttributes, flashMessageKey);

        return modelAndView;
    }

    private FlashMessageKey getFlashMessageKeyForUpdateResult(boolean successful) {
        return successful
                ? FlashMessageKey.SUCCESSFUL_PROFILE_UPDATE
                : FlashMessageKey.FAILED_PROFILE_UPDATE;
    }
}
