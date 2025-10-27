package com.example.gigmap_frontend_sprint1.bounded.shared.client

import com.example.gigmap_frontend_sprint1.bounded.communities.model.Community
import com.example.gigmap_frontend_sprint1.bounded.concerts.model.ConcertCreateRequest

import com.example.gigmap_frontend_sprint1.bounded.concerts.model.Concerts
import com.example.gigmap_frontend_sprint1.bounded.users.model.CreateDeviceTokenRequest
import com.example.gigmap_frontend_sprint1.bounded.users.model.LoginRequest
import com.example.gigmap_frontend_sprint1.bounded.users.model.LoginResponse
import com.example.gigmap_frontend_sprint1.bounded.communities.model.Post
import com.example.gigmap_frontend_sprint1.bounded.communities.model.PostCreateRequest
import com.example.gigmap_frontend_sprint1.bounded.users.model.RegisterRequest
import com.example.gigmap_frontend_sprint1.bounded.relatedevents.model.RelatedEvent
import com.example.gigmap_frontend_sprint1.bounded.users.model.Users
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