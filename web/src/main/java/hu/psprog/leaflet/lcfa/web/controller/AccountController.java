package hu.psprog.leaflet.lcfa.web.controller;

import hu.psprog.leaflet.api.rest.request.user.PasswordChangeRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UpdateProfileRequestModel;
import hu.psprog.leaflet.lcfa.core.domain.content.UserCommentsPageContent;
import hu.psprog.leaflet.lcfa.core.facade.AccountManagementFacade;
import hu.psprog.leaflet.lcfa.web.factory.ModelAndViewFactory;
import hu.psprog.leaflet.lcfa.web.model.FlashMessageKey;
import hu.psprog.leaflet.lcfa.web.model.ModelField;
import hu.psprog.leaflet.lcfa.web.ui.support.navigation.AccountNavigationBarSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

/**
 * Controller implementation for account related operations.
 *
 * @author Peter Smith
 */
@Controller
@RequestMapping(BaseController.PATH_PROFILE)
public class AccountController extends BaseController {

    private static final String VIEW_ACCOUNT_PROFILE = "view/account/profile";
    private static final String VIEW_ACCOUNT_PASSWORD_CHANGE = "view/account/pw_change";
    private static final String VIEW_ACCOUNT_COMMENTS = "view/account/comments";
    private static final String VIEW_ACCOUNT_DELETE = "view/account/delete";

    private static final int DEFAULT_PAGE_NUMBER = 1;
    private static final String COMMENT_PAGINATION_LINK_TEMPLATE = "/profile/my-comments/{page}";

    private final ModelAndViewFactory modelAndViewFactory;
    private final AccountManagementFacade accountManagementFacade;
    private final AccountNavigationBarSupport accountNavigationBarSupport;

    @Autowired
    public AccountController(ModelAndViewFactory modelAndViewFactory, AccountManagementFacade accountManagementFacade,
                             AccountNavigationBarSupport accountNavigationBarSupport) {
        this.modelAndViewFactory = modelAndViewFactory;
        this.accountManagementFacade = accountManagementFacade;
        this.accountNavigationBarSupport = accountNavigationBarSupport;
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
                .withAttribute(ModelField.NAVIGATION, accountNavigationBarSupport.profile())
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
                .withAttribute(ModelField.NAVIGATION, accountNavigationBarSupport.passwordChange())
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
     * GET /profile/my-comments[/{page}]
     * Renders list of authenticated user's existing comments.
     *
     * @return populated {@link ModelAndView} object
     */
    @GetMapping({PATH_MY_COMMENTS, PATH_MY_COMMENTS_PAGED})
    public ModelAndView renderComments(@PathVariable(value = "page", required = false) Optional<Integer> page) {

        UserCommentsPageContent pageContent = accountManagementFacade.getCommentsForUser(currentUserID(), extractPageNumber(page));

        return modelAndViewFactory.createForView(VIEW_ACCOUNT_COMMENTS)
                .withAttribute(ModelField.COMMENTS, pageContent.getComments())
                .withAttribute(ModelField.PAGINATION, pageContent.getPaginationAttributes())
                .withAttribute(ModelField.LINK_TEMPLATE, COMMENT_PAGINATION_LINK_TEMPLATE)
                .withAttribute(ModelField.NAVIGATION, accountNavigationBarSupport.myComments())
                .build();
    }

    /**
     * POST /profile/my-comments/delete
     * Processes a comment deletion request.
     *
     * @param commentID ID of the comment to be deleted
     * @param redirectAttributes redirection attributes
     * @return populated {@link ModelAndView} object
     */
    @PostMapping(PATH_MY_COMMENTS_DELETE)
    public ModelAndView deleteComment(@ModelAttribute("commentID") long commentID, RedirectAttributes redirectAttributes) {

        FlashMessageKey flashMessageKey = accountManagementFacade.deleteComment(commentID)
                ? FlashMessageKey.SUCCESSFUL_COMMENT_DELETION
                : FlashMessageKey.FAILED_COMMENT_DELETION;
        flash(redirectAttributes, flashMessageKey);

        return modelAndViewFactory.createRedirectionTo(PATH_PROFILE_MY_COMMENTS);
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
                .withAttribute(ModelField.NAVIGATION, accountNavigationBarSupport.accountDeletion())
                .build();
    }

    /**
     * POST /profile/delete-account
     * Processes an account deletion request.
     *
     * @param redirectAttributes redirection attributes
     * @return populated {@link ModelAndView} object
     */
    @PostMapping(PATH_DELETE_ACCOUNT)
    public ModelAndView processAccountDeletionRequest(RedirectAttributes redirectAttributes) {

        ModelAndView modelAndView;
        FlashMessageKey flashMessageKey;
        if (accountManagementFacade.deleteAccount(currentUserID())) {
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

    private int extractPageNumber(Optional<Integer> page) {
        return page.orElse(DEFAULT_PAGE_NUMBER);
    }
}
