package com.herokuapp.serverbugit.api.models.projects

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SingleProjectResponse(
    @Json(name = "response")
    val response:String,
    @Json(name = "result")
    val result:SingleProjectResponseDataType?
)