package com.example.gigmap_frontend_sprint1.bounded.users.model

data class LoginRequest(
    val emailOrUsername: String,
    val password: String
)