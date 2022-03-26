package com.herokuapp.serverbugit.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GlobalServerResponse(
    @Json(name = "response")
    val response:String,
    @Json(name = "result")
    val result:Any?
)
