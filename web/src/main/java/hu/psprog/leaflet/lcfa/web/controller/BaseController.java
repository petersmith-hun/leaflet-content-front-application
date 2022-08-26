package hu.psprog.leaflet.lcfa.web.controller;

import hu.psprog.leaflet.lcfa.core.exception.ContentNotFoundException;
import hu.psprog.leaflet.lcfa.core.exception.UserSessionInvalidationRequiredException;
import hu.psprog.leaflet.lcfa.web.model.FlashMessageKey;
import hu.psprog.leaflet.lcfa.web.model.ModelField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Base controller providing common methods.
 *
 * @author Peter Smith
 */
public class BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    private static final String HEADER_USER_AGENT = "User-Agent";
    private static final String SUBJECT_JWT_ATTRIBUTE = "sub";

    private static final String ERROR_401 = "error/401";
    private static final String ERROR_404 = "error/404";
    private static final String ERROR_500 = "error/500";

    public static final String PATH_FILTER_BY_CATEGORY_PAGED = "/category/{categoryID}/{categoryAlias}/page/{page}";
    public static final String PATH_FILTER_BY_CATEGORY = "/category/{categoryID}/{categoryAlias}";
    public static final String PATH_HOME = "/";
    public static final String PATH_HOME_PAGED = "/page/{page}";
    public static final String PATH_FILTER_BY_TAG = "/tag/{tagID}/{tagAlias}";
    public static final String PATH_FILTER_BY_TAG_PAGED = "/tag/{tagID}/{tagAlias}/page/{page}";
    public static final String PATH_FILTER_BY_CONTENT = "/content";
    public static final String PATH_FILTER_BY_CONTENT_PAGED = "/content/page/{page}";
    public static final String PATH_INTRODUCTION = "/introduction";
    public static final String PATH_CONTACT = "/contact";
    public static final String PATH_SIGN_IN = "/signin";
    public static final String PATH_SIGN_UP = "/signup";
    public static final String PATH_COMMENT = "/article/{link}/comment";
    public static final String PATH_ARTICLE_BY_LINK = "/article/{link}";
    public static final String PATH_CHANGE_PASSWORD = "/change-password";
    public static final String PATH_MY_COMMENTS = "/my-comments";
    public static final String PATH_MY_COMMENTS_PAGED = "/my-comments/{page}";
    public static final String PATH_MY_COMMENTS_DELETE = "/my-comments/delete";
    public static final String PATH_DELETE_ACCOUNT = "/delete-account";
    public static final String PATH_PROFILE = "/profile";
    public static final String PATH_PROFILE_DELETE_ACCOUNT = PATH_PROFILE + PATH_DELETE_ACCOUNT;
    public static final String PATH_PROFILE_CHANGE_PASSWORD = PATH_PROFILE + PATH_CHANGE_PASSWORD;
    public static final String PATH_PROFILE_MY_COMMENTS = PATH_PROFILE + PATH_MY_COMMENTS;

    /**
     * Sets the given flash message in the redirect attributes.
     *
     * @param redirectAttributes {@link RedirectAttributes} object for setting flash messages
     * @param flashMessageKey {@link FlashMessageKey} enum constant containing an actual message key (needs to be translated)
     */
    void flash(RedirectAttributes redirectAttributes, FlashMessageKey flashMessageKey) {
        redirectAttributes.addFlashAttribute(ModelField.FLASH.getFieldName(), flashMessageKey.getMessageKey());
    }

    /**
     * Returns current authenticated user's ID.
     *
     * @return user ID as {@link Long}
     */
    Long currentUserID() {

        Long userID = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2User principal = ((OAuth2AuthenticationToken) authentication).getPrincipal();
            userID = Optional.ofNullable(principal.getAttribute(SUBJECT_JWT_ATTRIBUTE))
                    .filter(subject -> subject instanceof String)
                    .map(subject -> Long.valueOf((String) subject))
                    .orElse(0L);
        }

        return userID;
    }

    /**
     * HTTP 401 handler. Triggered when the backend responds with 401.
     *
     * @param exception exception object to be logged
     * @return populated {@link ModelAndView} object
     */
    @ExceptionHandler(UserSessionInvalidationRequiredException.class)
    public ModelAndView handleUserSessionInvalidationRequiredException(UserSessionInvalidationRequiredException exception) {

        LOGGER.error("User session invalidation required - setting response status to 401 to trigger expiration filter", exception);

        return getErrorView(HttpStatus.UNAUTHORIZED);
    }

    /**
     * HTTP 404 handler. Triggered when a {@link ContentNotFoundException} is thrown.
     *
     * @param request {@link HttpServletRequest} object to extract request URI
     * @param exception exception object to be logged
     * @return populated {@link ModelAndView} object
     */
    @ExceptionHandler(ContentNotFoundException.class)
    public ModelAndView handleContentNotFoundException(HttpServletRequest request, ContentNotFoundException exception) {

        LOGGER.error("Content not found: {}", exception.getMessage());
        ModelAndView modelAndView = getErrorView(HttpStatus.NOT_FOUND);
        modelAndView.addObject("path", request.getRequestURI());

        return modelAndView;
    }

    /**
     * Handles any unhandled exceptions by showing the default (HTTP 500) error page.
     *
     * @param request {@link HttpServletRequest} object to extract request information for logging
     * @param exception exception object to be logged
     * @return populated {@link ModelAndView} object
     */
    @ExceptionHandler
    public ModelAndView defaultExceptionHandler(HttpServletRequest request, Exception exception) {

        LOGGER.error("Failed to fulfill request on path [{}] for user agent [{}] from address [{}]",
                request.getRequestURI(), request.getHeader(HEADER_USER_AGENT), request.getRemoteAddr(), exception);

        return getErrorView(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ModelAndView getErrorView(HttpStatus status) {

        ModelAndView modelAndView = new ModelAndView(getViewName(status));
        modelAndView.setStatus(status);

        return modelAndView;
    }

    private String getViewName(HttpStatus status) {

        String viewName;
        switch (status) {
            case UNAUTHORIZED:
                viewName = ERROR_401;
                break;
            case NOT_FOUND:
                viewName = ERROR_404;
                break;
            default:
                viewName = ERROR_500;
        }

        return viewName;
    }
}
