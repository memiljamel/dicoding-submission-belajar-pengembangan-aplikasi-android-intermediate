package com.dicoding.storyapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.storyapp.data.source.remote.network.ApiService
import com.dicoding.storyapp.data.source.remote.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class StoryRepository @Inject constructor(private val apiService: ApiService) {

    fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<RegisterResponse>> {
        return liveData {
            emit(Result.Loading)
            try {
                val response = apiService.register(name, email, password)
                emit(Result.Success(response))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.Error(e.message.toString()))
            }
        }
    }

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> {
        return liveData {
            emit(Result.Loading)
            try {
                val response = apiService.login(email, password)
                emit(Result.Success(response))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.Error(e.message.toString()))
            }
        }
    }

    fun addNewStory(
        token: String?,
        description: RequestBody,
        file: MultipartBody.Part
    ): LiveData<Result<AddNewStoryResponse>> {
        return liveData {
            emit(Result.Loading)
            try {
                val bearerToken = generateBearerToken(token)
                val response = apiService.addNewStory(bearerToken, description, file)
                emit(Result.Success(response))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.Error(e.message.toString()))
            }
        }
    }

    fun getAllStories(token: String?): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 12
            ),
            pagingSourceFactory = {
                val bearerToken = generateBearerToken(token)
                StoryPagingSource(apiService, bearerToken)
            }
        ).liveData
    }

    fun getAllStoriesWithLocation(token: String?): LiveData<Result<GetAllStoriesResponse>> {
        return liveData {
            emit(Result.Loading)
            try {
                val bearerToken = generateBearerToken(token)
                val response = apiService.getAllStories(bearerToken, null, null, 1)
                emit(Result.Success(response))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.Error(e.message.toString()))
            }
        }
    }

    private fun generateBearerToken(token: String?): String {
        return "Bearer $token"
    }
}