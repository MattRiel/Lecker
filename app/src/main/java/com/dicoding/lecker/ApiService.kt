package com.dicoding.lecker

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api")
    fun createPost(@Body requestBody: RequestBody): Call<RecipeData>
}

