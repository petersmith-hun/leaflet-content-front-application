package hu.psprog.leaflet.lcfa.web.thymeleaf.support.security;

import hu.psprog.leaflet.lcfa.web.model.FlashMessageKey;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * Unit tests for {@link FormLoginFlashMessageHelper}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class FormLoginFlashMessageHelperTest {

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private FormLoginFlashMessageHelper formLoginFlashMessageHelper;

    @ParameterizedTest
    @MethodSource("authResultKeyDataProvider")
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
        verifyNoInteractions(request);
    }

    private static Stream<Arguments> authResultKeyDataProvider() {

        return Stream.of(
                Arguments.of("success", FlashMessageKey.SUCCESSFUL_SIGN_IN),
                Arguments.of("failure", FlashMessageKey.FAILED_SIGN_IN),
                Arguments.of("signout", FlashMessageKey.SUCCESSFUL_SIGN_OUT)
        );
    }
}
