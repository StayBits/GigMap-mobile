package com.example.gigmap_frontend_sprint1.bounded.communities.model

data class Community(
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val posts: List<Int>,
    val members: List<Int>
)
