package com.dicoding.storyapp.utils

import com.dicoding.storyapp.data.source.remote.response.*

object DataDummy {

    fun generateDummyRegisterResponse(): RegisterResponse {
        return RegisterResponse(
            error = false,
            message = "User Created"
        )
    }

    fun generateDummyLoginResponse(): LoginResponse {
        val loginResult = LoginResult(
            name = "Guest Account",
            userId = "user-4dkbZACtKuHzFfKo",
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLTRka2JaQUN0S3VIekZmS28iLCJpYXQiOjE2NjMwNDg2MzZ9.3KYxKvE9K9Ko8RyGHlp66SqktCyhygSyMkPAOejMg6M"
        )

        return LoginResponse(
            loginResult = loginResult,
            error = false,
            message = "success"
        )
    }

    fun generateDummyAddNewStoryResponse(): AddNewStoryResponse {
        return AddNewStoryResponse(
            error = false,
            message = "success"
        )
    }

    fun generateDummyAllStoriesResponse(): GetAllStoriesResponse {
        val listStory = mutableListOf<ListStoryItem>()
        val error = false
        val message = "Stories fetched successfully"

        for (i in 0 until 100) {
            val story = ListStoryItem(
                id = "story-FvU4u0Vp2S3PMsFg",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                createdAt = "2022-01-08T06:34:18.598Z",
                name = "John Doe",
                description = "Lorem Ipsum",
                longitude = -16.002,
                latitude = -10.212
            )
            listStory.add(story)
        }

        return GetAllStoriesResponse(listStory, error, message)
    }

    fun generateDummyListStoryResponse(): List<ListStoryItem> {
        val items = arrayListOf<ListStoryItem>()

        for (i in 0 until 10) {
            val story = ListStoryItem(
                id = "story-FvU4u0Vp2S3PMsFg",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                createdAt = "2022-01-08T06:34:18.598Z",
                name = "John Doe",
                description = "Lorem Ipsum",
                longitude = -16.002,
                latitude = -10.212
            )
            items.add(story)
        }

        return items
    }
}