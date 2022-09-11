package com.dicoding.storyapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.AuthRepository
import com.dicoding.storyapp.data.Result
import com.dicoding.storyapp.data.StoryRepository
import com.dicoding.storyapp.data.source.remote.response.GetAllStoriesResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val storyRepository: StoryRepository
) : ViewModel() {

    fun getBearerToken(): Flow<String?> {
        return authRepository.getBearerToken()
    }

    suspend fun getAllStories(token: String?): Flow<Result<GetAllStoriesResponse>> {
        return storyRepository.getAllStories(token)
    }

    fun removeBearerToken() {
        viewModelScope.launch {
            authRepository.removeBearerToken()
        }
    }
}