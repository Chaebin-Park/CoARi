package com.cse.coari.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NewsRetrofitClient {
    private val retrofitClient: Retrofit.Builder by lazy {
        Retrofit.Builder()
                .baseUrl((CoariApi.DOMAIN))
                .addConverterFactory(GsonConverterFactory.create())
    }
    val newsRetrofitService: NewsRetrofitService by lazy {
        retrofitClient.build().create(NewsRetrofitService::class.java)
    }
}