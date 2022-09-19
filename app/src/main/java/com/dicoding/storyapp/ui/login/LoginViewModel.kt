package com.dicoding.storyapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.Result
import com.dicoding.storyapp.data.StoryRepository
import com.dicoding.storyapp.data.source.remote.response.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val storyRepository: StoryRepository) :
    ViewModel() {

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> {
        return storyRepository.login(email, password)
    }
}