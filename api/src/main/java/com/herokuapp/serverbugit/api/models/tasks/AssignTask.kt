package com.herokuapp.serverbugit.api.models.tasks

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class AssignTask(
    @Json(name = "t_id")
    val taskId:UUID,
    @Json(name = "user_id")
    val userId:UUID
)
