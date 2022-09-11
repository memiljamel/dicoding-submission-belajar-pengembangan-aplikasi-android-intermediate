package com.dicoding.storyapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.AuthRepository
import com.dicoding.storyapp.data.Result
import com.dicoding.storyapp.data.source.remote.response.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    suspend fun login(email: String, password: String): Flow<Result<LoginResponse>> {
        return authRepository.login(email, password)
    }

    fun saveBearerToken(token: String) {
        viewModelScope.launch {
            authRepository.saveBearerToken(token)
        }
    }
}