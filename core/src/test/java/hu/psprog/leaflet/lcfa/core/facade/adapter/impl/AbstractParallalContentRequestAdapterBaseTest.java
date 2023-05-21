package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.api.rest.response.category.CategoryListDataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.tag.TagListDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.service.CategoryBridgeService;
import hu.psprog.leaflet.bridge.service.TagBridgeService;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

/**
 * Abstract base test for parallel {@link ContentRequestAdapter} implementations.
 *
 * @author Peter Smith
 */
abstract class AbstractParallalContentRequestAdapterBaseTest {

    static final CategoryListDataModel CATEGORY_LIST_DATA_MODEL = CategoryListDataModel.getBuilder().build();
    static final WrapperBodyDataModel<TagListDataModel> WRAPPED_TAG_LIST_DATA_MODEL = WrapperBodyDataModel.<TagListDataModel>getBuilder()
            .withBody(TagListDataModel.getBuilder().build())
            .build();

    @Mock
    CategoryBridgeService categoryBridgeService;

    @Mock
    TagBridgeService tagBridgeService;

    private final AsyncTaskExecutor contentAdapterExecutor = new SimpleAsyncTaskExecutor();

    @BeforeEach
    public void setup() throws CommunicationFailureException {
        setMock("categoryBridgeService", categoryBridgeService);
        setMock("tagBridgeService", tagBridgeService);
        setMock("contentAdapterExecutor", contentAdapterExecutor);
    }

    void givenSuccessfulFilteringDataRetrieval() {
        try {
            given(categoryBridgeService.getPublicCategories()).willReturn(CATEGORY_LIST_DATA_MODEL);
            given(tagBridgeService.getAllPublicTags()).willReturn(WRAPPED_TAG_LIST_DATA_MODEL);
        } catch (CommunicationFailureException e) {
            fail();
        }
    }

    void givenFailedFilteringDataRetrieval() {
        try {
            doThrow(CommunicationFailureException.class).when(categoryBridgeService).getPublicCategories();
            doThrow(CommunicationFailureException.class).when(tagBridgeService).getAllPublicTags();
        } catch (CommunicationFailureException e) {
            fail();
        }
    }

    void setMock(String fieldName, Object mockObject) {
        Field mockField = ReflectionUtils.findField(ArticleContentRequestAdapter.class, fieldName);
        mockField.setAccessible(true);
        try {
            mockField.set(adapterUnderTest(), mockObject);
        } catch (IllegalAccessException e) {
            fail("Testcase configuration failed");
        }
    }

    abstract ContentRequestAdapter<?, ?> adapterUnderTest();
}
