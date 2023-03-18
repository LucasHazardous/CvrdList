package com.github.lucashazardous.cvrdlist.api

import com.github.lucashazardous.cvrdlist.SearchCards
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ServerApi {
    companion object {
        const val BASE_URL = "https://api.pokemontcg.io/v2/"
    }

    @GET("cards")
    fun searchCards(@Query("q") searchQuery: String, @Query("pageSize") pageSize: Int, @Query("page") page: Int): Call<SearchCards>
}