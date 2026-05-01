package hu.psprog.leaflet.lcfa.web.thymeleaf.support.markdown;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thymeleaf.processor.IProcessor;

import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link ExtendedLayoutDialect}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class ExtendedLayoutDialectTest {

    @InjectMocks
    private ExtendedLayoutDialect extendedLayoutDialect;

    @Test
    public void shouldGetProcessorsReturnMarkdownAttributeTagProcessingAlongWithTheOthers() {

        // when
        Set<IProcessor> result = extendedLayoutDialect.getProcessors("dialect");

        // then
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(17));
        assertThat(result.stream().anyMatch(iProcessor -> iProcessor instanceof MarkdownAttributeTagProcessor), is(true));
    }
}
