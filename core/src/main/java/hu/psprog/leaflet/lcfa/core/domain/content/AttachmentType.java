package hu.psprog.leaflet.lcfa.core.domain.content;

import org.springframework.util.AntPathMatcher;

import java.util.Arrays;

/**
 * Possible attachment types with their matching MIME types.
 *
 * @author Peter Smith
 */
public enum AttachmentType {
    IMAGE("image/*"),
    OTHER("*/*");

    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    private String mime;

    AttachmentType(String mime) {
        this.mime = mime;
    }

    public static AttachmentType mapByMime(String mime) {
        return Arrays.stream(values())
                .filter(attachmentType -> ANT_PATH_MATCHER.match(attachmentType.mime, mime))
                .findFirst()
                .orElse(OTHER);
    }
}
