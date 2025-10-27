package com.example.gigmap_frontend_sprint1.bounded.relatedevents.model

import com.example.gigmap_frontend_sprint1.bounded.concerts.model.Venue

data class RelatedEvent(
    val id: Int,
    val musicGenre: String,
    val status: String,
    val date: String,
    val venue: Venue,
    val attendees: List<Int>
)
