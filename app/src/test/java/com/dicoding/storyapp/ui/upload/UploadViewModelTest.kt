package com.dicoding.storyapp.ui.upload

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dicoding.storyapp.data.Result
import com.dicoding.storyapp.data.StoryRepository
import com.dicoding.storyapp.data.source.remote.response.AddNewStoryResponse
import com.dicoding.storyapp.utils.DataDummy
import com.dicoding.storyapp.utils.getOrAwaitValue
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
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class UploadViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var uploadViewModel: UploadViewModel
    private val dummyAddNewStoryResponse = DataDummy.generateDummyAddNewStoryResponse()

    @Before
    fun setUp() {
        uploadViewModel = UploadViewModel(storyRepository)
    }

    @Test
    fun `when upload should not null and return success`() {
        val description =
            "Ini adalah deskripsi sebuah gambar".toRequestBody("text/plain".toMediaType())
        val file = Mockito.mock(File::class.java)
        val requestImageFile = file.asRequestBody("image/jpg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            "nameFile",
            requestImageFile
        )

        val expectedResult = MutableLiveData<Result<AddNewStoryResponse>>()
        expectedResult.value = Result.Success(dummyAddNewStoryResponse)
        `when`(storyRepository.addNewStory(dummyToken, description, imageMultipart)).thenReturn(expectedResult)
        val actualResult = uploadViewModel.addNewStory(dummyToken, description, imageMultipart).getOrAwaitValue()
        Assert.assertNotNull(actualResult)
        Assert.assertTrue(actualResult is Result.Success)
    }

    companion object {
        private const val dummyToken =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLTRka2JaQUN0S3VIekZmS28iLCJpYXQiOjE2NjMwNDg2MzZ9.3KYxKvE9K9Ko8RyGHlp66SqktCyhygSyMkPAOejMg6M"
    }
}