package hu.psprog.leaflet.lcfa.core.config;

import hu.psprog.leaflet.lcfa.core.domain.content.request.OrderBy;
import hu.psprog.leaflet.lcfa.core.domain.content.request.OrderDirection;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Domain class for default pagination settings.
 *
 * @author Peter Smith
 */
@Component
@ConfigurationProperties(prefix = "pagination.default")
@Data
public class DefaultPaginationAttributes {

    private int limit;
    private OrderBy.Entry orderBy;
    private OrderDirection orderDirection;
}
