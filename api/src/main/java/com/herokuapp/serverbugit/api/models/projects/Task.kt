package com.herokuapp.serverbugit.api.models.projects

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class Task(
    @Json(name = "t_id")
    val taskId:UUID,
    @Json(name = "name")
    val name:String
)
