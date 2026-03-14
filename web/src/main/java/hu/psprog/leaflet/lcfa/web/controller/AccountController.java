package hu.psprog.leaflet.lcfa.web.controller;

import hu.psprog.leaflet.lcfa.core.config.PageConfigModel;
import hu.psprog.leaflet.lcfa.core.domain.content.UserCommentsPageContent;
import hu.psprog.leaflet.lcfa.core.facade.AccountManagementFacade;
import hu.psprog.leaflet.lcfa.web.factory.ModelAndViewFactory;
import hu.psprog.leaflet.lcfa.web.model.FlashMessageKey;
import hu.psprog.leaflet.lcfa.web.model.ModelField;
import hu.psprog.leaflet.lcfa.web.ui.support.navigation.AccountNavigationBarSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    private static final String VIEW_ACCOUNT_COMMENTS = "view/account/comments";

    private static final int DEFAULT_PAGE_NUMBER = 1;
    private static final String COMMENT_PAGINATION_LINK_TEMPLATE = "/profile/my-comments/{page}";

    private final ModelAndViewFactory modelAndViewFactory;
    private final AccountManagementFacade accountManagementFacade;
    private final AccountNavigationBarSupport accountNavigationBarSupport;
    private final PageConfigModel pageConfigModel;

    @Autowired
    public AccountController(ModelAndViewFactory modelAndViewFactory, AccountManagementFacade accountManagementFacade,
                             AccountNavigationBarSupport accountNavigationBarSupport, PageConfigModel pageConfigModel) {
        this.modelAndViewFactory = modelAndViewFactory;
        this.accountManagementFacade = accountManagementFacade;
        this.accountNavigationBarSupport = accountNavigationBarSupport;
        this.pageConfigModel = pageConfigModel;
    }

    /**
     * GET /profile
     * Renders profile main screen.
     *
     * @return populated {@link ModelAndView} object
     */
    @GetMapping
    public ModelAndView renderProfileMainScreen() {

        return modelAndViewFactory.createForView(VIEW_ACCOUNT_PROFILE)
                .withAttribute(ModelField.NAVIGATION, accountNavigationBarSupport.profile())
                .build();
    }

    /**
     * GET /profile/manage-profile
     * Redirects to the profile management screen of the integrated OAuth Authorization Server.
     *
     * @return populated {@link ModelAndView} object
     */
    @GetMapping(PATH_PROFILE_MANAGE)
    public ModelAndView redirectToProfileManagement() {
        return modelAndViewFactory.createRedirectionTo(pageConfigModel.getProfileManagementEndpoint());
    }

    /**
     * GET /profile/my-comments[/{page}]
     * Renders list of authenticated user's existing comments.
     *
     * @return populated {@link ModelAndView} object
     */
    @GetMapping({PATH_MY_COMMENTS, PATH_MY_COMMENTS_PAGED})
    public ModelAndView renderComments(@PathVariable(required = false) Optional<Integer> page) {

        UserCommentsPageContent pageContent = accountManagementFacade.getCommentsForUser(currentUserID(), extractPageNumber(page));

        return modelAndViewFactory.createForView(VIEW_ACCOUNT_COMMENTS)
                .withAttribute(ModelField.COMMENTS, pageContent.comments())
                .withAttribute(ModelField.PAGINATION, pageContent.paginationAttributes())
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

    private int extractPageNumber(Optional<Integer> page) {
        return page.orElse(DEFAULT_PAGE_NUMBER);
    }
}
