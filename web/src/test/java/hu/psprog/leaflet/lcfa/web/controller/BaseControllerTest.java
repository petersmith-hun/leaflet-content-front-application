package hu.psprog.leaflet.lcfa.web.controller;

import hu.psprog.leaflet.lcfa.core.exception.ContentNotFoundException;
import hu.psprog.leaflet.lcfa.core.exception.UserSessionInvalidationRequiredException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link BaseController}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class BaseControllerTest {

    private static final String REQUEST_URI = "/request/path";
    private static final String ERROR_401 = "error/401";
    private static final String ERROR_404 = "error/404";
    private static final String ERROR_500 = "error/500";
    private static final String PATH_MODEL_PARAMETER = "path";

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private BaseController baseController;

    @Test
    public void shouldHandleUserSessionInvalidationRequiredException() {

        // when
        ModelAndView result = baseController.handleUserSessionInvalidationRequiredException(request, new UserSessionInvalidationRequiredException(new RuntimeException()));

        // then
        assertThat(result, notNullValue());
        assertThat(result.getStatus(), equalTo(HttpStatus.UNAUTHORIZED));
        assertThat(result.getViewName(), equalTo(ERROR_401));
    }

    @Test
    public void shouldHandleContentNotFoundException() {

        // given
        given(request.getRequestURI()).willReturn(REQUEST_URI);

        // when
        ModelAndView result = baseController.handleContentNotFoundException(request, new ContentNotFoundException("not found"));

        // then
        assertThat(result, notNullValue());
        assertThat(result.getStatus(), equalTo(HttpStatus.NOT_FOUND));
        assertThat(result.getViewName(), equalTo(ERROR_404));
        assertThat(result.getModel().size(), equalTo(1));
        assertThat(result.getModel().get(PATH_MODEL_PARAMETER), equalTo(REQUEST_URI));
    }

    @Test
    public void shouldHandleAnyOtherExceptions() {

        // given
        given(request.getRequestURI()).willReturn(REQUEST_URI);

        // when
        ModelAndView result = baseController.defaultExceptionHandler(request, new Exception());

        // then
        assertThat(result, notNullValue());
        assertThat(result.getStatus(), equalTo(HttpStatus.INTERNAL_SERVER_ERROR));
        assertThat(result.getViewName(), equalTo(ERROR_500));
    }
}
