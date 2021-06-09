package com.cse.coari.retrofit

import com.cse.coari.data.*
import retrofit2.Call
import retrofit2.http.*

interface API {
    @Headers("Content-Type: */*")
    @GET("/api/notices/search")
    fun getNotice() : Call<NoticeDTO>

    @Headers("Content-Type: */*")
    @GET("/api/alarms/search")
    fun getAlarm() : Call<AlarmDTO>

    @Headers("Content-Type: */*")
    @GET("/api/news/search")
    fun getNews() : Call<GetNewsDTO>

    @Headers("Content-Type: */*")
    @DELETE("/api/alarms/{alarm_id}")
    fun deleteAlarm(@Path("alarm_id")id: String) : Call<AlarmDTO>

    @Headers("Content-Type: */*")
    @GET("/api/graduates/search")
    fun getHof() : Call<GetHofDTO>

    @Headers("Content-Type: */*")
    @GET("/api/graduates/{graduate_id}")
    fun postHof(@Path("graduate_id")id: String) : Call<PostHofDTO>
}