package com.dicoding.storyapp.utils

import com.dicoding.storyapp.data.source.remote.network.ApiService
import com.dicoding.storyapp.data.source.remote.response.AddNewStoryResponse
import com.dicoding.storyapp.data.source.remote.response.GetAllStoriesResponse
import com.dicoding.storyapp.data.source.remote.response.LoginResponse
import com.dicoding.storyapp.data.source.remote.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeApiService : ApiService {
    private val dummyRegisterResponse = DataDummy.generateDummyRegisterResponse()
    private val dummyLoginResponse = DataDummy.generateDummyLoginResponse()
    private val dummyAllStoriesResponse = DataDummy.generateDummyAllStoriesResponse()
    private val dummyAddNewStoryResponse = DataDummy.generateDummyAddNewStoryResponse()

    override suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return dummyRegisterResponse
    }

    override suspend fun login(email: String, password: String): LoginResponse {
        return dummyLoginResponse
    }

    override suspend fun addNewStory(
        token: String,
        description: RequestBody,
        file: MultipartBody.Part,
        lat: Double?,
        lon: Double?
    ): AddNewStoryResponse {
        return dummyAddNewStoryResponse
    }

    override suspend fun getAllStories(
        token: String,
        page: Int?,
        size: Int?,
        location: Int?
    ): GetAllStoriesResponse {
        return dummyAllStoriesResponse
    }
}