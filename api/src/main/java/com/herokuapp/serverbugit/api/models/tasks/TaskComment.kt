package com.herokuapp.serverbugit.api.models.tasks

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class TaskComment(
    @Json(name = "tc_id")
    val taskCommentId:UUID,
    @Json(name = "t_id")
    val taskId:UUID,
    @Json(name = "user_id")
    val userId:UUID,
    @Json(name = "user_name")
    val userName:String,
    @Json(name = "comment")
    val comment:String,
    @Json(name = "created_at")
    val createdAt:String
)
