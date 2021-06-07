package com.cse.coari.retrofit

import com.cse.coari.data.NewsDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface NewsRetrofitService {
    @GET("/api/news")
    fun getNewsDTO(): Call<NewsDTO>
}