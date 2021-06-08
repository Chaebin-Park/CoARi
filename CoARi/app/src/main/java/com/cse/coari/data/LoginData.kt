package com.cse.coari.data

import java.io.Serializable

data class LoginData(
    var type: LoginType,
    var name: String,
    var email: String = "NONE",
    var profImg: String = "NONE",
    var token: String = "NONE"
) : Serializable
