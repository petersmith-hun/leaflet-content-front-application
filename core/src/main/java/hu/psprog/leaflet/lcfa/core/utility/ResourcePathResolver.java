package hu.psprog.leaflet.lcfa.core.utility;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Resolves path of downloadable contents.
 *
 * @author Peter Smith
 */
@Component
public class ResourcePathResolver {

    private static final String RESOURCE_SERVER_URL_PART = "{resource-server-url}";
    private static final String RESOURCE_ID_PART = "{resource-id}";
    private static final String[] RESOURCE_URL_PARTS = {RESOURCE_SERVER_URL_PART, RESOURCE_ID_PART};
    private static final String RESOURCE_PATTERN = RESOURCE_SERVER_URL_PART + "/" + RESOURCE_ID_PART;
    private static final String PREFIX_SLASH = "/";

    private String resourceServerUrl;

    public ResourcePathResolver(@Value("${page-config.resource-server-url}") String resourceServerUrl) {
        this.resourceServerUrl = resourceServerUrl;
    }

    /**
     * Revolves resources meant to be downloaded directly by concatenating proper resource server URL and file reference.
     *
     * @param resourceID file reference from backend
     * @return resolved URL
     */
    public String resolve(String resourceID) {
        return StringUtils.replaceEach(RESOURCE_PATTERN, RESOURCE_URL_PARTS, new String[] {resourceServerUrl, normalizeResourceID(resourceID)});
    }

    /**
     * Resolves (image) resources embedded in articles' markdown source by replacing {@code {resource-server-url}} placeholder in image links.
     *
     * @param rawContent markdown article source
     * @return markdown article source with resolved resource server
     */
    public String resolveInMarkdownSource(String rawContent) {
        return StringUtils.replace(rawContent, RESOURCE_SERVER_URL_PART, resourceServerUrl);
    }

    private String normalizeResourceID(String resourceID) {

        String normalizedResourceID = resourceID;
        if (resourceID.startsWith(PREFIX_SLASH)) {
            normalizedResourceID = resourceID.substring(1);
        }

        return normalizedResourceID;
    }
}
