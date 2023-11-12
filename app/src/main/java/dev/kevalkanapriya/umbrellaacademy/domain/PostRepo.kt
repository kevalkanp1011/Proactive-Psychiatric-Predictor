package dev.kevalkanapriya.umbrellaacademy.domain

import dev.kevalkanapriya.umbrellaacademy.data.remote.PostService
import dev.kevalkanapriya.umbrellaacademy.data.remote.dto.PostRequest
import dev.kevalkanapriya.umbrellaacademy.data.remote.dto.PostResponse

class PostRepo(private val postService: PostService) {

    suspend fun getPosts(): List<PostResponse> {
        return postService.getPosts()
    }
    suspend fun createPost(postRequest: PostRequest): PostResponse? {
        return postService.createPost(postRequest)
    }
}