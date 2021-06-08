package com.cse.coari.data

import com.google.gson.annotations.SerializedName

data class NoticeDTOItem(
    val author: String,
    val date: String,
    @SerializedName("notice_id")    val noticeId: Int,
    val title: String,
    val url: String
)