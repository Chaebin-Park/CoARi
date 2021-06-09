package com.cse.coari.data

data class GetHofDetailDTO(
    val company: String,
    val companyInfo: String,
    val fileURL: String,
    val graduate_id: Int,
    val image: String,
    val interview_content: String,
    val name: String,
    val work: String
)