package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.api.rest.response.category.CategoryDataModel;
import hu.psprog.leaflet.lcfa.core.domain.content.CategorySummary;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link CategorySummaryConverter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class CategorySummaryConverterTest {

    private static final long ID = 1L;
    private static final String TITLE = "title";
    private static final CategoryDataModel SOURCE_OBJECT = CategoryDataModel.getBuilder()
            .withID(ID)
            .withTitle(TITLE)
            .build();
    private static final String ALIAS = "alias";
    private static final CategorySummary EXPECTED_CONVERTED_OBJECT = CategorySummary.builder()
            .id(ID)
            .title(TITLE)
            .alias(ALIAS)
            .build();

    @Mock
    private LinkAliasGenerator linkAliasGenerator;

    @InjectMocks
    private CategorySummaryConverter converter;

    @Test
    public void shouldConvert() {

        // given
        given(linkAliasGenerator.generateAlias(TITLE)).willReturn(ALIAS);

        // when
        CategorySummary result = converter.convert(SOURCE_OBJECT);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(EXPECTED_CONVERTED_OBJECT));
    }
}
