package hu.psprog.leaflet.lcfa.core.domain.request;

import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request model class for posting comments on articles.
 *
 * @author Peter Smith
 */
@Data
public class ArticleCommentRequest {

    @NotNull
    @Min(1L)
    private Long entryId;

    @NotEmpty
    @Size(max = 2000)
    private String message;

    @NotEmpty
    private String recaptchaToken;
}
