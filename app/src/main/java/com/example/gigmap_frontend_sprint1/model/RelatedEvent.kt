package com.example.gigmap_frontend_sprint1.model

data class RelatedEvent(
    val id: Int,
    val musicGenre: String,
    val status: String,
    val date: String,
    val venue: Venue,
    val attendees: List<Int>
)
