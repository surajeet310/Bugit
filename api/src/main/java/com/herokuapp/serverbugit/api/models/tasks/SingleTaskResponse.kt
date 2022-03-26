package com.herokuapp.serverbugit.api.models.tasks

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SingleTaskResponse(
    @Json(name = "response")
    val response:String,
    @Json(name = "result")
    val result:SingleTaskResponseDataType?
)
