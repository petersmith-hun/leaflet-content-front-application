package hu.psprog.leaflet.lcfa.core.config;

import hu.psprog.leaflet.lcfa.core.domain.content.StaticPageType;
import lombok.Data;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Configuration wrapper class for "page-config" configuration parameters.
 *
 * @author Peter Smith
 */
@Data
@Setter
@Component
@ConfigurationProperties(prefix = "page-config")
public class PageConfigModel {

    private String siteName;
    private String contactMail;
    private String copyright;
    private String resourceServerUrl;
    private String recaptchaSiteKey;
    private String ogImage;
    private Map<DateFormatType, DateTimeFormatter> dateFormatterMap;
    private Map<StaticPageType, String> staticPageMapping = new HashMap<>();
    private CommonPageDataCacheConfigModel commonPageDataCache;
    private String logoutEndpoint;
    private GoogleSiteTrackingConfigModel googleSiteTracking;

    public void setDateFormat(Map<DateFormatType, String> dateFormat) {
        if (Objects.isNull(dateFormatterMap)) {
            this.dateFormatterMap = dateFormat.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> DateTimeFormatter.ofPattern(entry.getValue())));
        }
    }
}
