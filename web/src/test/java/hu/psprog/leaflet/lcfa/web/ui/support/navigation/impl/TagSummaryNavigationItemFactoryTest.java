package hu.psprog.leaflet.lcfa.web.ui.support.navigation.impl;

import hu.psprog.leaflet.lcfa.core.domain.content.TagSummary;
import hu.psprog.leaflet.lcfa.web.model.NavigationItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link TagSummaryNavigationItemFactory}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class TagSummaryNavigationItemFactoryTest {

    private static final String TAG_NAME = "Tag name";
    private static final TagSummary TAG_SUMMARY = TagSummary.builder()
            .id(1L)
            .name(TAG_NAME)
            .alias("tag-alias")
            .build();
    private static final NavigationItem EXPECTED_NAVIGATION_ITEM = NavigationItem.build("/tag/1/tag-alias", TAG_NAME);

    @InjectMocks
    private TagSummaryNavigationItemFactory tagSummaryNavigationItemFactory;

    @Test
    public void shouldCreateNavigationItem() {

        // when
        NavigationItem result = tagSummaryNavigationItemFactory.create(TAG_SUMMARY);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(EXPECTED_NAVIGATION_ITEM));
    }

    @Test
    public void shouldForModelClassReturnClassInstance() {

        // when
        Class<TagSummary> result = tagSummaryNavigationItemFactory.forModelClass();

        // then
        assertThat(result, notNullValue());
    }
}
