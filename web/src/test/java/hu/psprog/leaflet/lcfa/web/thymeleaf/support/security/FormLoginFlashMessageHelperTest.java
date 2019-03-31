package hu.psprog.leaflet.lcfa.web.thymeleaf.support.security;

import hu.psprog.leaflet.lcfa.web.model.FlashMessageKey;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Unit tests for {@link FormLoginFlashMessageHelper}.
 *
 * @author Peter Smith
 */
@RunWith(JUnitParamsRunner.class)
public class FormLoginFlashMessageHelperTest {

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private FormLoginFlashMessageHelper formLoginFlashMessageHelper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @Parameters(source = AuthResultKeyProvider.class)
    public void shouldGetMessageKey(String authResult, FlashMessageKey expectedFlashMessageKey) {

        // given
        given(request.getParameter("auth")).willReturn(authResult);

        // when
        String result = formLoginFlashMessageHelper.getMessageKey(request, null);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(expectedFlashMessageKey.getMessageKey()));
    }

    @Test
    public void shouldGetMessageKeyReturnNullForInvalidAuthResultKey() {

        // given
        given(request.getParameter("auth")).willReturn("invalid");

        // when
        String result = formLoginFlashMessageHelper.getMessageKey(request, null);

        // then
        assertThat(result, nullValue());
    }

    @Test
    public void shouldGetMessageKeyKeepNonNullFlashMessageKeyIntact() {

        // given
        String currentFlashMessageKey = "current.flash.message.key";

        // when
        String result = formLoginFlashMessageHelper.getMessageKey(request, currentFlashMessageKey);

        // then
        assertThat(result, equalTo(currentFlashMessageKey));
        verifyZeroInteractions(request);
    }

    public static class AuthResultKeyProvider {

        public static Object[] provide() {
            return new Object[] {
                    new Object[] {"success", FlashMessageKey.SUCCESSFUL_SIGN_IN},
                    new Object[] {"failure", FlashMessageKey.FAILED_SIGN_IN},
                    new Object[] {"signout", FlashMessageKey.SUCCESSFUL_SIGN_OUT}
            };
        }
    }
}
