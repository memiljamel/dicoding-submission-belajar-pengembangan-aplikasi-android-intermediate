package com.dicoding.storyapp.data

import com.dicoding.storyapp.data.source.local.datastore.AuthPreferences
import com.dicoding.storyapp.data.source.remote.network.ApiService
import com.dicoding.storyapp.data.source.remote.response.LoginResponse
import com.dicoding.storyapp.data.source.remote.response.RegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val authPreferences: AuthPreferences
) {

    suspend fun register(
        name: String,
        email: String,
        password: String
    ): Flow<Result<RegisterResponse>> {
        return flow {
            emit(Result.Loading)
            try {
                val response = apiService.register(name, email, password)
                emit(Result.Success(response))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun login(email: String, password: String): Flow<Result<LoginResponse>> {
        return flow {
            emit(Result.Loading)
            try {
                val response = apiService.login(email, password)
                emit(Result.Success(response))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getBearerToken(): Flow<String?> {
        return authPreferences.getBearerToken()
    }

    suspend fun saveBearerToken(token: String) {
        authPreferences.saveBearerToken(token)
    }

    suspend fun removeBearerToken() {
        authPreferences.removeBearerToken()
    }
}
