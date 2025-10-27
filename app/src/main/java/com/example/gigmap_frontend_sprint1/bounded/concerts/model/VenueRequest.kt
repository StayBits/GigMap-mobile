package com.example.gigmap_frontend_sprint1.bounded.concerts.model

data class VenueRequest(
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val capacity: Int
)
