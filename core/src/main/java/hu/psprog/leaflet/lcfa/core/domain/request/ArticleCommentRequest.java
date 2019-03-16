package hu.psprog.leaflet.lcfa.core.domain.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Request model class for posting comments on articles.
 *
 * @author Peter Smith
 */
@Data
public class ArticleCommentRequest {

    private String name;
    private String email;

    @NotNull
    @Min(1L)
    private Long entryId;

    @NotEmpty
    @Size(max = 2000)
    private String message;

    @NotEmpty
    private String recaptchaToken;
}
