package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.api.rest.response.tag.TagDataModel;
import hu.psprog.leaflet.api.rest.response.tag.TagListDataModel;
import hu.psprog.leaflet.lcfa.core.domain.content.TagSummary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link TagSummaryListConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class TagSummaryListConverterTest {

    private static final long TAG_ID = 2L;
    private static final String TAG_NAME = "tag name";
    private static final String TAG_ALIAS = "tag-alias";
    private static final TagListDataModel TAG_LIST_DATA_MODEL = TagListDataModel.getBuilder()
            .withTags(List.of(TagDataModel.getBuilder()
                    .withId(TAG_ID)
                    .withName(TAG_NAME)
                    .build()))
            .build();
    private static final TagSummary TAG_SUMMARY = TagSummary.builder()
            .id(TAG_ID)
            .name(TAG_NAME)
            .alias(TAG_ALIAS)
            .build();

    @Mock
    private LinkAliasGenerator linkAliasGenerator;

    @InjectMocks
    private TagSummaryListConverter tagSummaryListConverter;

    @Test
    public void shouldConvertFromTagListDataModel() {

        // given
        given(linkAliasGenerator.generateAlias(TAG_NAME)).willReturn(TAG_ALIAS);

        // when
        List<TagSummary> result = tagSummaryListConverter.convert(TAG_LIST_DATA_MODEL);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(Collections.singletonList(TAG_SUMMARY)));
    }

    @Test
    public void shouldConvertFromListOfTagDataModels() {

        // given
        given(linkAliasGenerator.generateAlias(TAG_NAME)).willReturn(TAG_ALIAS);

        // when
        List<TagSummary> result = tagSummaryListConverter.convert(TAG_LIST_DATA_MODEL.tags());

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(Collections.singletonList(TAG_SUMMARY)));
    }
}
