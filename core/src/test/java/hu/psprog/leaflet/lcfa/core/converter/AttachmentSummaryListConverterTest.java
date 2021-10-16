package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.api.rest.response.file.FileDataModel;
import hu.psprog.leaflet.lcfa.core.domain.content.AttachmentSummary;
import hu.psprog.leaflet.lcfa.core.domain.content.AttachmentType;
import hu.psprog.leaflet.lcfa.core.utility.ResourcePathResolver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link AttachmentSummaryListConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class AttachmentSummaryListConverterTest {

    private static final String ORIGINAL_FILENAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String REFERENCE = "reference";
    private static final String ACCEPTED_AS = "image/jpg";
    private static final List<FileDataModel> SOURCE_OBJECT = Collections.singletonList(FileDataModel.getBuilder()
            .withOriginalFilename(ORIGINAL_FILENAME)
            .withDescription(DESCRIPTION)
            .withReference(REFERENCE)
            .withAcceptedAs(ACCEPTED_AS)
            .build());
    private static final String LINK = "link";
    private static final AttachmentSummary EXPECTED_CONVERTED_OBJECT = AttachmentSummary.builder()
            .name(ORIGINAL_FILENAME)
            .description(DESCRIPTION)
            .link(LINK)
            .type(AttachmentType.IMAGE)
            .build();

    @Mock
    private ResourcePathResolver resourcePathResolver;

    @InjectMocks
    private AttachmentSummaryListConverter converter;

    @Test
    public void shouldConvert() {

        // given
        given(resourcePathResolver.resolve(REFERENCE)).willReturn(LINK);

        // when
        List<AttachmentSummary> result = converter.convert(SOURCE_OBJECT);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(Collections.singletonList(EXPECTED_CONVERTED_OBJECT)));
    }
}
