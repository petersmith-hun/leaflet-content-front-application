package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.api.rest.response.entry.EntryDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryListDataModel;
import hu.psprog.leaflet.api.rest.response.user.UserDataModel;
import hu.psprog.leaflet.lcfa.core.domain.content.AuthorSummary;
import hu.psprog.leaflet.lcfa.core.domain.content.EntrySummary;
import hu.psprog.leaflet.lcfa.core.formatter.DateFormatterUtility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link EntrySummaryListConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class EntrySummaryListConverterTest {

    private static final String USERNAME = "username";
    private static final ZonedDateTime CREATED = ZonedDateTime.now();
    private static final ZonedDateTime PUBLISHED = ZonedDateTime.now().plusDays(1);
    private static final String FORMATTED_CREATED_DATE = "formatted-created-date";
    private static final String FORMATTED_PUBLISHED_DATE = "formatted-published-date";
    private static final String LINK = "link";
    private static final String TITLE = "title";
    private static final String PROLOGUE = "prologue";
    private static final EntryDataModel ENTRY_DATA_MODEL_WITHOUT_PUBLISHED_DATE = EntryDataModel.getBuilder()
            .withLink(LINK)
            .withTitle(TITLE)
            .withUser(UserDataModel.getBuilder()
                    .withUsername(USERNAME)
                    .build())
            .withCreated(CREATED)
            .withPrologue(PROLOGUE)
            .build();
    private static final EntrySummary ENTRY_SUMMARY_WITHOUT_PUBLISHED_DATE = EntrySummary.builder()
            .link(LINK)
            .title(TITLE)
            .author(new AuthorSummary(USERNAME))
            .creationDate(FORMATTED_CREATED_DATE)
            .prologue(PROLOGUE)
            .build();
    private static final EntryDataModel ENTRY_DATA_MODEL_WITH_PUBLISHED_DATE = EntryDataModel.getBuilder()
            .withLink(LINK)
            .withTitle(TITLE)
            .withUser(UserDataModel.getBuilder()
                    .withUsername(USERNAME)
                    .build())
            .withCreated(CREATED)
            .withPublished(PUBLISHED)
            .withPrologue(PROLOGUE)
            .build();
    private static final EntrySummary ENTRY_SUMMARY_WITH_PUBLISHED_DATE = EntrySummary.builder()
            .link(LINK)
            .title(TITLE)
            .author(new AuthorSummary(USERNAME))
            .creationDate(FORMATTED_PUBLISHED_DATE)
            .prologue(PROLOGUE)
            .build();

    @Mock
    private DateFormatterUtility dateFormatterUtility;

    @InjectMocks
    private EntrySummaryListConverter converter;

    @Test
    public void shouldConvertWithoutPublishedDate() {

        // given
        given(dateFormatterUtility.formatGeneral(CREATED)).willReturn(FORMATTED_CREATED_DATE);
        EntryListDataModel entryListDataModel = EntryListDataModel.getBuilder()
                .withEntries(List.of(ENTRY_DATA_MODEL_WITHOUT_PUBLISHED_DATE))
                .build();

        // when
        List<EntrySummary> result = converter.convert(entryListDataModel);

        // then
        assertThat(result, notNullValue());
        assertThat(result, hasItem(ENTRY_SUMMARY_WITHOUT_PUBLISHED_DATE));
    }

    @Test
    public void shouldConvertWithPublishedDate() {

        // given
        given(dateFormatterUtility.formatGeneral(PUBLISHED)).willReturn(FORMATTED_PUBLISHED_DATE);
        EntryListDataModel entryListDataModel = EntryListDataModel.getBuilder()
                .withEntries(List.of(ENTRY_DATA_MODEL_WITH_PUBLISHED_DATE))
                .build();

        // when
        List<EntrySummary> result = converter.convert(entryListDataModel);

        // then
        assertThat(result, notNullValue());
        assertThat(result, hasItem(ENTRY_SUMMARY_WITH_PUBLISHED_DATE));
    }
}
