package com.dicoding.storyapp.ui.splash

import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {

    fun getBearerToken(): Flow<String?> {
        return authRepository.getBearerToken()
    }
}
