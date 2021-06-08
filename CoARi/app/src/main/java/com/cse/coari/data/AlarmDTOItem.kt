package com.cse.coari.data

import com.google.gson.annotations.SerializedName

data class AlarmDTOItem(
    @SerializedName("alarm_id")     val alarmId: Int,
    @SerializedName("author")       val author: String,
    @SerializedName("read_mark")    val readMark: String,
    @SerializedName("send_time")    val sendTime: String,
    @SerializedName("title")        val title: String
)