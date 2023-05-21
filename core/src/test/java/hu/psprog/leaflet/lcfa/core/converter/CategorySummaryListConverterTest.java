package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.api.rest.response.category.CategoryDataModel;
import hu.psprog.leaflet.api.rest.response.category.CategoryListDataModel;
import hu.psprog.leaflet.lcfa.core.domain.content.CategorySummary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link CategorySummaryListConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class CategorySummaryListConverterTest {

    private static final CategoryDataModel CATEGORY_DATA_MODEL = CategoryDataModel.getBuilder().withId(1L).build();
    private static final CategorySummary CATEGORY_SUMMARY = CategorySummary.builder().id(1L).build();
    private static final CategoryListDataModel SOURCE_OBJECT = CategoryListDataModel.getBuilder().withCategories(List.of(CATEGORY_DATA_MODEL)).build();
    private static final List<CategorySummary> EXPECTED_CONVERTED_OBJECT = Collections.singletonList(CATEGORY_SUMMARY);

    @Mock
    private CategorySummaryConverter categorySummaryConverter;

    @InjectMocks
    private CategorySummaryListConverter converter;

    @Test
    public void shouldConvert() {

        // given
        given(categorySummaryConverter.convert(CATEGORY_DATA_MODEL)).willReturn(CATEGORY_SUMMARY);

        // when
        List<CategorySummary> result = converter.convert(SOURCE_OBJECT);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(EXPECTED_CONVERTED_OBJECT));
    }
}
