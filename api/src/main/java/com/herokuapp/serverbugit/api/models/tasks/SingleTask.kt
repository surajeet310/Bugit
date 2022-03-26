package com.herokuapp.serverbugit.api.models.tasks

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class SingleTask(
    @Json(name = "t_id")
    val taskId:UUID,
    @Json(name = "p_id")
    val projectId:UUID,
    @Json(name = "name")
    val name:String,
    @Json(name = "descp")
    val desc:String,
    @Json(name = "assignee")
    val assignee:UUID,
    @Json(name = "assigned_to")
    val assignedTo:UUID,
    @Json(name = "created_at")
    val createdAt:String,
    @Json(name = "deadline")
    val deadline:String,
    @Json(name = "tech")
    val tech:String
)