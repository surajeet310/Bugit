package com.herokuapp.serverbugit.api.models.projects

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SingleProjectResponseDataType(
    @Json(name = "project")
    val project:SingleProject,
    @Json(name = "tasks")
    val tasks:List<Task>?
)
