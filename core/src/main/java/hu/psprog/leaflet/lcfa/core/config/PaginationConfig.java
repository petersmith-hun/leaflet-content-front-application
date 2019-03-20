package hu.psprog.leaflet.lcfa.core.config;

import hu.psprog.leaflet.lcfa.core.domain.content.request.OrderBy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Pagination settings configuration.
 *
 * @author Peter Smith
 */
@Configuration
public class PaginationConfig {

    @Bean
    @ConfigurationProperties(prefix = "pagination.default.entry")
    public DefaultPaginationAttributes<OrderBy.Entry> entryDefaultPaginationAttributes() {
        return new EntryDefaultPaginationAttributes();
    }

    @Bean
    @ConfigurationProperties(prefix = "pagination.default.comment")
    public DefaultPaginationAttributes<OrderBy.Comment> commentDefaultPaginationAttributes() {
        return new CommentDefaultPaginationAttributes();
    }
}
