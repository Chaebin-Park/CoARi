package com.cse.coari.data

import com.google.gson.annotations.SerializedName

data class PostNewsData(
    @SerializedName("author")   val author: String,
    @SerializedName("content")  val content: String,
    @SerializedName("date")     val date: String,
    @SerializedName("news_thumbnail")   val thumbnail: String,
    @SerializedName("title")    val title: String,
    @SerializedName("url")      val url: String
)