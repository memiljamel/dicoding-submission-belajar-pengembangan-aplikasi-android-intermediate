package com.dicoding.storyapp.data

import com.dicoding.storyapp.data.source.remote.network.ApiService
import com.dicoding.storyapp.data.source.remote.response.AddNewStoryResponse
import com.dicoding.storyapp.data.source.remote.response.GetAllStoriesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class StoryRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun addNewStory(
        token: String?,
        description: RequestBody,
        file: MultipartBody.Part
    ): Flow<Result<AddNewStoryResponse>> {
        return flow {
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

    suspend fun getAllStories(token: String?): Flow<Result<GetAllStoriesResponse>> {
        return flow {
            emit(Result.Loading)
            try {
                val bearerToken = generateBearerToken(token)
                val response = apiService.getAllStories(bearerToken)
                emit(Result.Success(response))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    private fun generateBearerToken(token: String?): String {
        return "Bearer $token"
    }
}