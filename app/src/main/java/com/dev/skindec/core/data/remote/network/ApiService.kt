package com.dev.skindec.core.data.remote.network

import com.dev.skindec.core.data.remote.response.UserResponse
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("users")
    fun getAllUsers(): Call<List<UserResponse>>

    @GET("users/{user_id}")
    fun getUser(
        @Path("user_id") userId: Int
    ): Call<UserResponse>

    @Headers("Content-Type: application/json")
    @POST("users")
    fun register(
        @Body requestBody: JsonObject
    ): Call<UserResponse>

    @Multipart
    @POST("upload")
    fun uploadImage(
        @Part image: MultipartBody.Part
    ): Call<Any>
}