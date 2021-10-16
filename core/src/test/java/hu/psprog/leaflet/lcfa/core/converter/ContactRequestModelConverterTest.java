package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.lcfa.core.domain.request.ContactRequestModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link ContactRequestModelConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class ContactRequestModelConverterTest {

    private static final ContactRequestModel SOURCE_OBJECT = new ContactRequestModel();
    private static final hu.psprog.leaflet.api.rest.request.contact.ContactRequestModel EXPECTED_CONVERTED_OBJECT =
            new hu.psprog.leaflet.api.rest.request.contact.ContactRequestModel();

    private static final String MESSAGE = "message";
    private static final String EMAIL = "email";
    private static final String NAME = "name";

    static {
        SOURCE_OBJECT.setMessage(MESSAGE);
        SOURCE_OBJECT.setEmail(EMAIL);
        SOURCE_OBJECT.setName(NAME);

        EXPECTED_CONVERTED_OBJECT.setMessage(MESSAGE);
        EXPECTED_CONVERTED_OBJECT.setEmail(EMAIL);
        EXPECTED_CONVERTED_OBJECT.setName(NAME);
    }

    @InjectMocks
    private ContactRequestModelConverter converter;

    @Test
    public void shouldConvert() {

        // when
        hu.psprog.leaflet.api.rest.request.contact.ContactRequestModel result = converter.convert(SOURCE_OBJECT);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(EXPECTED_CONVERTED_OBJECT));
    }
}
