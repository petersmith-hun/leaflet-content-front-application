package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.api.rest.request.user.UserInitializeRequestModel;
import hu.psprog.leaflet.lcfa.core.domain.request.SignUpRequestModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link SignUpRequestConverter}.
 * 
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class SignUpRequestConverterTest {

    private static final SignUpRequestModel SIGN_UP_REQUEST_MODEL = new SignUpRequestModel();
    private static final UserInitializeRequestModel EXPECTED_USER_INITIALIZE_REQUEST_MODEL = new UserInitializeRequestModel();

    private static final String USERNAME = "username";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String PASSWORD_CONFIRMATION = "password confirmation";
    private static final Locale DEFAULT_LOCALE = Locale.forLanguageTag("HU");

    static {
        SIGN_UP_REQUEST_MODEL.setUsername(USERNAME);
        SIGN_UP_REQUEST_MODEL.setEmail(EMAIL);
        SIGN_UP_REQUEST_MODEL.setPassword(PASSWORD);
        SIGN_UP_REQUEST_MODEL.setPasswordConfirmation(PASSWORD_CONFIRMATION);

        EXPECTED_USER_INITIALIZE_REQUEST_MODEL.setUsername(USERNAME);
        EXPECTED_USER_INITIALIZE_REQUEST_MODEL.setEmail(EMAIL);
        EXPECTED_USER_INITIALIZE_REQUEST_MODEL.setPassword(PASSWORD);
        EXPECTED_USER_INITIALIZE_REQUEST_MODEL.setPasswordConfirmation(PASSWORD_CONFIRMATION);
        EXPECTED_USER_INITIALIZE_REQUEST_MODEL.setDefaultLocale(DEFAULT_LOCALE);
    }
    
    @InjectMocks
    private SignUpRequestConverter signUpRequestConverter;
    
    @Test
    public void shouldConvert() {
        
        // when
        UserInitializeRequestModel result = signUpRequestConverter.convert(SIGN_UP_REQUEST_MODEL);
        
        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(EXPECTED_USER_INITIALIZE_REQUEST_MODEL));
    }
}
