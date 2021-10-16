package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.api.rest.response.user.ExtendedUserDataModel;
import hu.psprog.leaflet.lcfa.core.domain.account.AccountBaseInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link AccountBaseInfoConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class AccountBaseInfoConverterTest {

    private static final String EMAIL = "email";
    private static final String USERNAME = "username";
    private static final String LOCALE = "locale";
    private static final ExtendedUserDataModel SOURCE_OBJECT = ExtendedUserDataModel.getExtendedBuilder()
            .withEmail(EMAIL)
            .withUsername(USERNAME)
            .withLocale(LOCALE)
            .build();
    private static final AccountBaseInfo EXPECTED_CONVERTED_OBJECT = AccountBaseInfo.builder()
            .email(EMAIL)
            .username(USERNAME)
            .locale(LOCALE)
            .build();

    @InjectMocks
    private AccountBaseInfoConverter converter;

    @Test
    public void shouldConvert() {

        // when
        AccountBaseInfo result = converter.convert(SOURCE_OBJECT);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(EXPECTED_CONVERTED_OBJECT));
    }
}
