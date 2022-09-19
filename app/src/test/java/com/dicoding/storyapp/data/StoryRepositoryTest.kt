package com.dicoding.storyapp.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dicoding.storyapp.data.source.remote.network.ApiService
import com.dicoding.storyapp.utils.DataDummy
import com.dicoding.storyapp.utils.FakeApiService
import com.dicoding.storyapp.utils.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryRepositoryTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainCoroutineRule()

    private lateinit var apiService: ApiService
    private lateinit var storyRepository: StoryRepository

    @Before
    fun setUp() {
        apiService = FakeApiService()
        storyRepository = StoryRepository(apiService)
    }

    @Test
    fun `when register successfully`() = runTest {
        val expectedResult = DataDummy.generateDummyRegisterResponse()
        val actualResult = apiService.register(
            dummyName,
            dummyEmail,
            dummyPassword
        )
        Assert.assertNotNull(actualResult)
        Assert.assertEquals(expectedResult.message, actualResult.message)
    }

    @Test
    fun `when login successfully`() = runTest {
        val expectedResult = DataDummy.generateDummyLoginResponse()
        val actualResult = apiService.login(
            dummyEmail,
            dummyPassword
        )
        Assert.assertNotNull(actualResult)
        Assert.assertEquals(expectedResult.message, actualResult.message)
    }

    @Test
    fun `when add new story successfully`() = runTest {
        val description =
            "Ini adalah deskripsi sebuah gambar".toRequestBody("text/plain".toMediaType())
        val file = mock(File::class.java)
        val requestImageFile = file.asRequestBody("image/jpg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            "nameFile",
            requestImageFile
        )

        val expectedResult = DataDummy.generateDummyAddNewStoryResponse()
        val actualResult = apiService.addNewStory(
            dummyToken,
            description,
            imageMultipart,
            dummyLatitude,
            dummyLongitude
        )
        Assert.assertNotNull(actualResult)
        Assert.assertEquals(expectedResult.message, actualResult.message)
    }

    @Test
    fun `when get all story successfully`() = runTest {
        val expectedResult = DataDummy.generateDummyAllStoriesResponse()
        val actualResult = apiService.getAllStories(dummyToken)
        Assert.assertNotNull(actualResult)
        Assert.assertEquals(expectedResult.listStory.size, actualResult.listStory.size)
    }

    companion object {
        private const val dummyName = "Guest Account"
        private const val dummyEmail = "guest@domain.com"
        private const val dummyPassword = "secret"
        private const val dummyLatitude = 1.23
        private const val dummyLongitude = 1.23
        private const val dummyToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLTRka2JaQUN0S3VIekZmS28iLCJpYXQiOjE2NjMwNDg2MzZ9.3KYxKvE9K9Ko8RyGHlp66SqktCyhygSyMkPAOejMg6M"
    }
}
