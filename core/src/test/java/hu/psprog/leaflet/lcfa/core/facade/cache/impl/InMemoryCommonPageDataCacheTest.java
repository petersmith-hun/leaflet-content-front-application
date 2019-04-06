package hu.psprog.leaflet.lcfa.core.facade.cache.impl;

import hu.psprog.leaflet.lcfa.core.config.CommonPageDataCacheConfigModel;
import hu.psprog.leaflet.lcfa.core.config.PageConfigModel;
import hu.psprog.leaflet.lcfa.core.domain.common.CommonPageData;
import hu.psprog.leaflet.lcfa.core.domain.common.SEOAttributes;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link InMemoryCommonPageDataCache}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class InMemoryCommonPageDataCacheTest {

    private static final CommonPageData COMMON_PAGE_DATA = CommonPageData.builder()
            .seo(SEOAttributes.builder()
                    .pageTitle("page title")
                    .build())
            .build();
    private static final int CACHE_TIMEOUT = 1000;
    private static final ChronoUnit CACHE_TIMEOUT_UNIT = ChronoUnit.MILLIS;
    private static final int THREAD_SLEEP = CACHE_TIMEOUT + 100;

    private InMemoryCommonPageDataCache inMemoryCommonPageDataCache;

    @Before
    public void setup() {
        CommonPageDataCacheConfigModel commonPageDataCacheConfigModel = new CommonPageDataCacheConfigModel();
        commonPageDataCacheConfigModel.setCacheTimeout(CACHE_TIMEOUT);
        commonPageDataCacheConfigModel.setCacheTimeoutUnit(CACHE_TIMEOUT_UNIT);

        PageConfigModel pageConfigModel = new PageConfigModel();
        pageConfigModel.setCommonPageDataCache(commonPageDataCacheConfigModel);

        inMemoryCommonPageDataCache = new InMemoryCommonPageDataCache(pageConfigModel);
    }

    @Test
    public void shouldGetCachedReturnEmptyOptionalIfNeverUpdated() {

        // when
        Optional<CommonPageData> result = inMemoryCommonPageDataCache.getCached();

        // then
        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void shouldGetCachedReturnCachedInstance() {

        // given
        inMemoryCommonPageDataCache.update(COMMON_PAGE_DATA);

        // when
        Optional<CommonPageData> result = inMemoryCommonPageDataCache.getCached();

        // then
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), equalTo(COMMON_PAGE_DATA));
    }

    @Test
    public void shouldGetCachedReturnEmptyOptionalIfLastUpdateDateIsNull() {

        // given
        inMemoryCommonPageDataCache.update(COMMON_PAGE_DATA);
        clearLastUpdateField();

        // when
        Optional<CommonPageData> result = inMemoryCommonPageDataCache.getCached();

        // then
        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void shouldGetCachedReturnEmptyOptionalForOutdatedCachedInstance() throws InterruptedException {

        // given
        inMemoryCommonPageDataCache.update(COMMON_PAGE_DATA);
        Thread.sleep(THREAD_SLEEP); // wait a bit more than the cache's timeout interval, so becomes outdated

        // when
        Optional<CommonPageData> result = inMemoryCommonPageDataCache.getCached();

        // then
        assertThat(result.isPresent(), is(false));
    }

    private void clearLastUpdateField() {
        Field lastUpdateField = ReflectionUtils.findField(InMemoryCommonPageDataCache.class, "lastUpdate");
        lastUpdateField.setAccessible(true);
        ReflectionUtils.setField(lastUpdateField, inMemoryCommonPageDataCache, null);
    }
}
