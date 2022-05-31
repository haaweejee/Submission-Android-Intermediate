package id.haaweejee.storyapp.service.api

import id.haaweejee.storyapp.service.data.addstory.AddStoryResponse
import id.haaweejee.storyapp.service.data.liststory.StoryResponse
import id.haaweejee.storyapp.service.data.login.LoginRequest
import id.haaweejee.storyapp.service.data.login.LoginResponse
import id.haaweejee.storyapp.service.data.register.RegisterRequest
import id.haaweejee.storyapp.service.data.register.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("register")
    suspend fun userRegister(
        @Body body: RegisterRequest
    ): Response<RegisterResponse>

    @POST("login")
    suspend fun userLogin(
        @Body body: LoginRequest
    ): Response<LoginResponse>

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") bearer: String,
        @Query("page") page : Int,
        @Query("size") size : Int,
        @Query("location") location: String = "1"
    ): StoryResponse

    @GET("stories")
    suspend fun getListMapStories(
        @Header("Authorization") bearer: String,
        @Query("location") location: String = "1"
    ): Response<StoryResponse>

    @Multipart
    @POST("stories")
    suspend fun postStories(
        @Header("Authorization") bearer: String,
        @Part file : MultipartBody.Part,
        @Part("description") description : RequestBody,
        @Part("lat") lat : RequestBody? = null,
        @Part("lon") lon : RequestBody? = null,
    ): Response<AddStoryResponse>
}