package com.example.gigmap_frontend_sprint1.bounded.users.model

data class RegisterRequest(
    val email: String,
    val username: String,
    val password: String,
    val role: String
)