package hu.psprog.leaflet.lcfa.web.controller;

import hu.psprog.leaflet.jwt.auth.support.domain.AuthenticationUserDetailsModel;
import hu.psprog.leaflet.jwt.auth.support.domain.JWTTokenAuthentication;
import hu.psprog.leaflet.lcfa.core.exception.ContentNotFoundException;
import hu.psprog.leaflet.lcfa.web.model.FlashMessageKey;
import hu.psprog.leaflet.lcfa.web.model.ModelField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Base controller providing common methods.
 *
 * @author Peter Smith
 */
public class BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    private static final String HEADER_USER_AGENT = "User-Agent";

    private static final String ERROR_404 = "error/404";
    private static final String ERROR_500 = "error/500";

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
        if (authentication instanceof JWTTokenAuthentication) {
            userID = ((AuthenticationUserDetailsModel) authentication.getDetails()).getId();
        }

        return userID;
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
        return status == HttpStatus.NOT_FOUND
                ? ERROR_404
                : ERROR_500;
    }
}
