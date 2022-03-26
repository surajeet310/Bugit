package com.herokuapp.serverbugit.api.models.tasks

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SingleTaskResponseDataType(
    @Json(name = "task")
    val task:SingleTask,
    @Json(name = "comments")
    val comments:List<TaskComment>?
)
