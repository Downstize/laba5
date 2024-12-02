package com.example.laba5.data

import retrofit2.http.GET
import retrofit2.http.Query
import com.example.laba5.models.Character

interface ApiService {
    @GET("characters")
    suspend fun getCharacters(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int = 50
    ): List<Character>
}