package com.dicoding.storyapp.di

import com.dicoding.storyapp.data.remote.network.ApiConfig
import com.dicoding.storyapp.data.source.remote.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun providedApiService(): ApiService {
        return ApiConfig.getApiService()
    }
}
