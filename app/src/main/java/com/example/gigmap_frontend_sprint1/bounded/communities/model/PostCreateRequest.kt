package com.example.gigmap_frontend_sprint1.bounded.communities.model


//awar
data class PostCreateRequest(
    val content: String,
    val imageUrl: String?,
    val communityId: Int,
    val userId: Int
)