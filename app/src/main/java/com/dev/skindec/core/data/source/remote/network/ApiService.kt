package com.dev.skindec.core.data.source.remote.network

import com.dev.skindec.core.data.source.remote.response.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("users")
    fun getAllUsers(): Call<List<UserResponse>>

    @GET("users/{user_id}")
    fun getUser(
        @Path("user_id") userId: Int
    ): Call<UserResponse>

    @FormUrlEncoded
    @POST("users")
    fun register(
        @Field("age") age: Int,
        @Field("name") name: String,
        @Field("sex") sex: String
    ): Call<UserResponse>
}