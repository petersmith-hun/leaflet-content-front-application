package hu.psprog.leaflet.lcfa.core.formatter;

import hu.psprog.leaflet.lcfa.core.config.DateFormatType;
import hu.psprog.leaflet.lcfa.core.config.PageConfigModel;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link DateFormatterUtility}.
 *
 * @author Peter Smith
 */
public class DateFormatterUtilityTest {

    private static final Map<DateFormatType, String> DATE_FORMAT_TYPE_MAP = Map.of(
            DateFormatType.GENERAL, "yyyy-LL-dd",
            DateFormatType.COMMENTS, "yyyy-LL-dd@HH:mm");
    private static final ZonedDateTime ZONED_DATE_TIME = ZonedDateTime.of(2019, 4, 3, 22, 30, 45, 123, ZoneId.systemDefault());
    private static final String EXPECTED_FORMATTED_GENERAL_DATE = "2019-04-03";
    private static final String EXPECTED_FORMATTED_COMMENT_DATE = "2019-04-03@22:30";

    private DateFormatterUtility dateFormatterUtility;

    @BeforeEach
    public void setup() {
        PageConfigModel pageConfigModel = new PageConfigModel();
        pageConfigModel.setDateFormat(DATE_FORMAT_TYPE_MAP);

        dateFormatterUtility = new DateFormatterUtility(pageConfigModel);
    }

    @Test
    public void shouldFormatGeneral() {

        // when
        String result = dateFormatterUtility.formatGeneral(ZONED_DATE_TIME);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(EXPECTED_FORMATTED_GENERAL_DATE));
    }

    @Test
    public void shouldFormatGeneralReturnEmptyForNullInput() {

        // when
        String result = dateFormatterUtility.formatGeneral(null);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(StringUtils.EMPTY));
    }

    @Test
    public void shouldFormatComments() {

        // when
        String result = dateFormatterUtility.formatComments(ZONED_DATE_TIME);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(EXPECTED_FORMATTED_COMMENT_DATE));
    }

    @Test
    public void shouldFormatCommentsReturnEmptyForNullInput() {

        // when
        String result = dateFormatterUtility.formatComments(null);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(StringUtils.EMPTY));
    }
}
