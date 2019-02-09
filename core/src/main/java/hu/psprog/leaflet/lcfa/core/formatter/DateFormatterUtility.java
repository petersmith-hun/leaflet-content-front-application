package hu.psprog.leaflet.lcfa.core.formatter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Utility class for formatting dates.
 *
 * @author Peter Smith
 */
@Component
@ConfigurationProperties(prefix = "page-config")
public class DateFormatterUtility {

    private Map<DateFormatType, DateTimeFormatter> dateFormatterMap;

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

    public void setDateFormat(Map<DateFormatType, String> dateFormat) {
        if (Objects.isNull(dateFormatterMap)) {
            this.dateFormatterMap = dateFormat.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> DateTimeFormatter.ofPattern(entry.getValue())));
        }
    }

    private String doFormat(ZonedDateTime dateTime, DateFormatType dateFormatType) {
        return Optional.ofNullable(dateTime)
                .map(zonedDateTime -> zonedDateTime.withZoneSameInstant(ZoneId.systemDefault()).format(dateFormatterMap.get(dateFormatType)))
                .orElse(StringUtils.EMPTY);
    }

    private enum DateFormatType {
        GENERAL,
        COMMENTS
    }
}
