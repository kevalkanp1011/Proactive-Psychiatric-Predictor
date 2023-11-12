package dev.kevalkanapriya.umbrellaacademy.data.remote

import dev.kevalkanapriya.umbrellaacademy.data.remote.dto.PostRequest
import dev.kevalkanapriya.umbrellaacademy.data.remote.dto.PostResponse
import io.ktor.client.HttpClient

interface PostService {
    suspend fun getPosts(): List<PostResponse>
    suspend fun createPost(postRequest: PostRequest): PostResponse?

    companion object {
        fun create(client: HttpClient): PostService {
            return PostServiceImpl(client)
        }
    }


}