package com.herokuapp.serverbugit.api.models.users

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserResponse(
    @Json(name = "response")
    val response:String,
    @Json(name = "result")
    val result:UserDetails?
)
