package com.dicoding.storyapp.ui.upload

import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.AuthRepository
import com.dicoding.storyapp.data.Result
import com.dicoding.storyapp.data.StoryRepository
import com.dicoding.storyapp.data.source.remote.response.AddNewStoryResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val storyRepository: StoryRepository
) : ViewModel() {

    fun getBearerToken(): Flow<String?> {
        return authRepository.getBearerToken()
    }

    suspend fun addNewStory(
        token: String?,
        description: RequestBody,
        file: MultipartBody.Part
    ): Flow<Result<AddNewStoryResponse>> {
        return storyRepository.addNewStory(token, description, file)
    }
}