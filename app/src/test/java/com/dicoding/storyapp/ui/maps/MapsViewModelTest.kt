package com.dicoding.storyapp.ui.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dicoding.storyapp.data.Result
import com.dicoding.storyapp.data.StoryRepository
import com.dicoding.storyapp.data.source.remote.response.GetAllStoriesResponse
import com.dicoding.storyapp.utils.DataDummy
import com.dicoding.storyapp.utils.getOrAwaitValue
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var mapsViewModel: MapsViewModel
    private val dummyAllStoriesResponse = DataDummy.generateDummyAllStoriesResponse()

    @Before
    fun setUp() {
        mapsViewModel = MapsViewModel(storyRepository)
    }

    @Test
    fun `when get story with location should not null and return stories fetched successfully`() {
        val expectedResult = MutableLiveData<Result<GetAllStoriesResponse>>()
        expectedResult.value = Result.Success(dummyAllStoriesResponse)
        `when`(storyRepository.getAllStoriesWithLocation(dummyToken)).thenReturn(expectedResult)
        val actualResult = mapsViewModel.getAllStoriesWithLocation(dummyToken).getOrAwaitValue()
        verify(storyRepository).getAllStoriesWithLocation(dummyToken)
        Assert.assertNotNull(actualResult)
        Assert.assertTrue(actualResult is Result.Success)
    }

    companion object {
        private const val dummyToken =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLTRka2JaQUN0S3VIekZmS28iLCJpYXQiOjE2NjMwNDg2MzZ9.3KYxKvE9K9Ko8RyGHlp66SqktCyhygSyMkPAOejMg6M"
    }
}