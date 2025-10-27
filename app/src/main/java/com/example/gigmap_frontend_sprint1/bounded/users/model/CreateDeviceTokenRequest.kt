package com.example.gigmap_frontend_sprint1.bounded.users.model

data class CreateDeviceTokenRequest(
    val userId: Int,
    val token: String
)