package com.example.remote

import com.google.gson.Gson
import com.example.model.Character


class RemoteDataSource(
        private val apiService: ApiServices,
        override val gson: Gson
) : SafeNetworkRequestCaller {

    suspend fun getAllCharacters(limit: Int, offset: Int): NetworkResult<List<Character>> {
        return request { apiService.getAllCharacters(limit,offset) }
    }

}