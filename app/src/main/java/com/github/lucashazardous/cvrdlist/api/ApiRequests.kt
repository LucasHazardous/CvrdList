package com.github.lucashazardous.cvrdlist.api

import android.util.Log
import com.github.lucashazardous.cvrdlist.SearchCards
import com.github.lucashazardous.cvrdlist.loadedSearchCards
import com.github.lucashazardous.cvrdlist.toCard
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiRequests {
    companion object {
        private val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        private val retrofit = Retrofit.Builder()
            .baseUrl(ServerApi.BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        private val api: ServerApi = retrofit.create(ServerApi::class.java)

        fun searchCards(searchQuery: String, pageSize: Int, page: Int) {
            api.searchCards(searchQuery, pageSize, page).enqueue(object: Callback<SearchCards> {
                override fun onResponse(
                    call: Call<SearchCards>,
                    response: Response<SearchCards>
                ) {
                    if (response.isSuccessful) {
                        Log.d("ApiRequests", "onResponse: ${response.body()}")
                        val searchCards = response.body()!!
                        for(searchCard in searchCards.data) {
                            loadedSearchCards.add(searchCard.toCard())
                        }
                    }
                }

                override fun onFailure(call: Call<SearchCards>, t: Throwable) {
                    Log.e("ApiRequests", "onFailure: ${t.message}")
                }
            })
        }
    }
}