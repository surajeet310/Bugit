package com.herokuapp.serverbugit.api.models.workspaces

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class Project(
    @Json(name = "p_id")
    val projectId:UUID,
    @Json(name = "name")
    val name:String,
    @Json(name = "task_count")
    val taskCount:Int,
    @Json(name = "member_count")
    val memberCount:Int
)
