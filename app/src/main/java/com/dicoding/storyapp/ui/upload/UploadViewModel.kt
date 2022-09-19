package com.dicoding.storyapp.ui.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.Result
import com.dicoding.storyapp.data.StoryRepository
import com.dicoding.storyapp.data.source.remote.response.AddNewStoryResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(private val storyRepository: StoryRepository) :
    ViewModel() {

    fun addNewStory(
        token: String?,
        description: RequestBody,
        file: MultipartBody.Part
    ): LiveData<Result<AddNewStoryResponse>> {
        return storyRepository.addNewStory(token, description, file)
    }
}