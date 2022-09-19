package com.dicoding.storyapp.ui.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dicoding.storyapp.data.Result
import com.dicoding.storyapp.data.StoryRepository
import com.dicoding.storyapp.data.source.remote.response.RegisterResponse
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
class RegisterViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var registerViewModel: RegisterViewModel
    private val dummyRegisterResponse = DataDummy.generateDummyRegisterResponse()

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(storyRepository)
    }

    @Test
    fun `when register should not null and return user created`() {
        val expectedResult = MutableLiveData<Result<RegisterResponse>>()
        expectedResult.value = Result.Success(dummyRegisterResponse)
        `when`(storyRepository.register(dummyName, dummyEmail, dummyPassword)).thenReturn(expectedResult)
        val actualResult = registerViewModel.register(dummyName, dummyEmail, dummyPassword).getOrAwaitValue()
        verify(storyRepository).register(dummyName, dummyEmail, dummyPassword)
        Assert.assertNotNull(actualResult)
        Assert.assertTrue(actualResult is Result.Success)
    }

    companion object {
        private const val dummyName = "Guest Account"
        private const val dummyEmail = "guest@domain.com"
        private const val dummyPassword = "secret"
    }
}