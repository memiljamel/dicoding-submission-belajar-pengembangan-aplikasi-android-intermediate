package com.dicoding.storyapp.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dicoding.storyapp.data.Result
import com.dicoding.storyapp.data.StoryRepository
import com.dicoding.storyapp.data.source.remote.response.LoginResponse
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
class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var loginViewModel: LoginViewModel
    private val dummyLoginResponse = DataDummy.generateDummyLoginResponse()

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(storyRepository)
    }

    @Test
    fun `when login should not null and return success`() {
        val expectedResult = MutableLiveData<Result<LoginResponse>>()
        expectedResult.value = Result.Success(dummyLoginResponse)
        `when`(storyRepository.login(dummyEmail, dummyPassword)).thenReturn(expectedResult)
        val actualResult = loginViewModel.login(dummyEmail, dummyPassword).getOrAwaitValue()
        verify(storyRepository).login(dummyEmail, dummyPassword)
        Assert.assertNotNull(actualResult)
        Assert.assertTrue(actualResult is Result.Success)
    }

    companion object {
        private const val dummyEmail = "guest@domain.com"
        private const val dummyPassword = "secret"
    }
}