package com.herokuapp.serverbugit.api.models.workspaces

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HomeResponse(
    @Json(name = "response")
    val response:String,
    @Json(name = "result")
    val result:List<Home>?
)
