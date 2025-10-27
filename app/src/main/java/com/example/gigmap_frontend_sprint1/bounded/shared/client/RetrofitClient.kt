package com.example.gigmap_frontend_sprint1.bounded.shared.client

import com.example.gigmap_frontend_sprint1.bounded.shared.client.WebService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val webService: WebService by lazy {
        Retrofit.Builder()
            .baseUrl("https://gigmap-api.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WebService::class.java)
    }
}