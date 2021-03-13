package com.example.remote


import retrofit2.http.GET
import retrofit2.http.Query
import com.example.model.Character

interface ApiServices {
    companion object {
        const val URL = "https://www.breakingbadapi.com"
    }

    @GET("/api/characters")
    suspend fun getAllCharacters(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ):List<Character>
}
