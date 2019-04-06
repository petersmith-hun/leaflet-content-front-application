package hu.psprog.leaflet.lcfa.core.formatter;

import hu.psprog.leaflet.lcfa.core.config.DateFormatType;
import hu.psprog.leaflet.lcfa.core.config.PageConfigModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

/**
 * Utility class for formatting dates.
 *
 * @author Peter Smith
 */
@Component
public class DateFormatterUtility {

    private Map<DateFormatType, DateTimeFormatter> dateFormatterMap;

    @Autowired
    public DateFormatterUtility(PageConfigModel pageConfigModel) {
        this.dateFormatterMap = pageConfigModel.getDateFormatterMap();
    }

    /**
     * Formats general dates.
     *
     * @param dateTime {@link ZonedDateTime} instance
     * @return formatted date string or empty string if null
     */
    public String formatGeneral(ZonedDateTime dateTime) {
        return doFormat(dateTime, DateFormatType.GENERAL);
    }

    /**
     * Formats comment dates.
     *
     * @param dateTime {@link ZonedDateTime} instance
     * @return formatted date string or empty string if null
     */
    public String formatComments(ZonedDateTime dateTime) {
        return doFormat(dateTime, DateFormatType.COMMENTS);
    }

    private String doFormat(ZonedDateTime dateTime, DateFormatType dateFormatType) {
        return Optional.ofNullable(dateTime)
                .map(zonedDateTime -> zonedDateTime.withZoneSameInstant(ZoneId.systemDefault())
                        .format(dateFormatterMap.get(dateFormatType)))
                .orElse(StringUtils.EMPTY);
    }
}
