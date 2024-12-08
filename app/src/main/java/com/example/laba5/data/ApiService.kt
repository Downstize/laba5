package com.example.laba5.data

import retrofit2.http.GET
import retrofit2.http.Query
import com.example.laba5.models.Character
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Response

interface ApiService {
    @GET("characters")
    suspend fun getCharacters(): Response<List<Character>>

    @GET("characters")
    suspend fun getCharacters(@Query("page") page: Int = 1): Response<List<Character>>
}