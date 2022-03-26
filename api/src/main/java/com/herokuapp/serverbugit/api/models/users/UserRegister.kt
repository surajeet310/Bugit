package com.herokuapp.serverbugit.api.models.users

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserRegister(
    @Json(name = "fname")
    val fname:String,
    @Json(name = "lname")
    val lname:String,
    @Json(name = "email")
    val email:String,
    @Json(name = "password")
    val password:String
)
