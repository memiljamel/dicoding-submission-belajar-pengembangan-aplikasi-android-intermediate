package com.dicoding.storyapp.ui.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.dicoding.storyapp.data.StoryRepository
import com.dicoding.storyapp.data.source.remote.response.ListStoryItem
import com.dicoding.storyapp.ui.upload.UploadViewModel
import com.dicoding.storyapp.utils.DataDummy
import com.dicoding.storyapp.utils.MainCoroutineRule
import com.dicoding.storyapp.utils.PagedTestDataSource
import com.dicoding.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var storyViewModel: StoryViewModel

    @Before
    fun setUp() {
        storyViewModel = StoryViewModel(storyRepository)
    }

    @Test
    fun `when get story should not null`() = runTest {
        val dummyListStoryResponse = DataDummy.generateDummyListStoryResponse()
        val data = PagedTestDataSource.snapshot(dummyListStoryResponse)
        val story = MutableLiveData<PagingData<ListStoryItem>>()
        story.value = data
        `when`(storyRepository.getAllStories(dummyToken)).thenReturn(story)
        val actualResult = storyViewModel.getAllStories(dummyToken).getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = mainCoroutineRule.testDispatcher,
            workerDispatcher = mainCoroutineRule.testDispatcher
        )
        differ.submitData(actualResult)
        advanceUntilIdle()
        verify(storyRepository).getAllStories(dummyToken)
        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyListStoryResponse.size, differ.snapshot().size)
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

    companion object {
        private const val dummyToken =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLTRka2JaQUN0S3VIekZmS28iLCJpYXQiOjE2NjMwNDg2MzZ9.3KYxKvE9K9Ko8RyGHlp66SqktCyhygSyMkPAOejMg6M"
    }
}