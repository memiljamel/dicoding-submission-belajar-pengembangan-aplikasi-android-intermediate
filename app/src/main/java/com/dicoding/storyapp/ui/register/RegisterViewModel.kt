package com.dicoding.storyapp.ui.register

import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.AuthRepository
import com.dicoding.storyapp.data.Result
import com.dicoding.storyapp.data.source.remote.response.RegisterResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {

    suspend fun register(
        name: String,
        email: String,
        password: String
    ): Flow<Result<RegisterResponse>> {
        return authRepository.register(name, email, password)
    }
}