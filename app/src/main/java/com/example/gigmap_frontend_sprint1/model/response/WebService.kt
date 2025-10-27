package com.example.gigmap_frontend_sprint1.model.response

import com.example.gigmap_frontend_sprint1.model.Community
import com.example.gigmap_frontend_sprint1.model.ConcertCreateRequest

import com.example.gigmap_frontend_sprint1.model.Concerts
import com.example.gigmap_frontend_sprint1.model.CreateDeviceTokenRequest
import com.example.gigmap_frontend_sprint1.model.LoginRequest
import com.example.gigmap_frontend_sprint1.model.LoginResponse
import com.example.gigmap_frontend_sprint1.model.Post
import com.example.gigmap_frontend_sprint1.model.PostCreateRequest
import com.example.gigmap_frontend_sprint1.model.RegisterRequest
import com.example.gigmap_frontend_sprint1.model.RelatedEvent
import com.example.gigmap_frontend_sprint1.model.Users
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface WebService {

    @POST("api/v1/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<Users>

    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
    @GET("api/v1/concerts")
    suspend fun getConcerts(): Response<List<Concerts>>

    @POST("api/v1/concerts")
    suspend fun createConcert(@Body concert: ConcertCreateRequest): Response<Concerts>

    @GET("api/v1/users")
    suspend fun getUsers(): Response<List<Users>>

    @GET("api/v1/communities")
    suspend fun getCommunities(): Response<List<Community>>

    @POST("api/v1/communities")
    suspend fun createCommunity(@Body community: Community): Response<Community>

    @GET("api/v1/posts")
    suspend fun getPosts(): Response<List<Post>>


    @POST("api/v1/posts")
    suspend fun createPost(@Body postRequest: PostCreateRequest): Response<Post>

    @POST("api/v1/posts/{postId}/like")
    suspend fun likePost(
        @Path("postId") postId: Long,
        @Query("userId") userId: Long
    ): Response<Void>

    @DELETE("api/v1/posts/{postId}/unlike")
    suspend fun unlikePost(
        @Path("postId") postId: Long,
        @Query("userId") userId: Long
    ): Response<Void>



    @GET("api/v1/relatedEvents")
    suspend fun getRelatedEvents(): Response<List<RelatedEvent>>

    @GET("api/v1/concerts/genre/{genre}")
    suspend fun getConcertsByGenre(@Path("genre") genre: String): Response<List<Concerts>>

    @POST("api/v1/device_tokens")
    suspend fun createDeviceToken(@Body deviceTokenRequest: CreateDeviceTokenRequest): Response<Unit>
}