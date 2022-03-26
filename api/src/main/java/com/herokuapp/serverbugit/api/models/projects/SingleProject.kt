package com.herokuapp.serverbugit.api.models.projects

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class SingleProject(
    @Json(name = "p_id")
    val projectId:UUID,
    @Json(name = "name")
    val name:String,
    @Json(name = "descp")
    val desc:String,
    @Json(name = "task_count")
    val taskCount:Int,
    @Json(name = "member_count")
    val memberCount:Int,
    @Json(name = "created_at")
    val createdAt:String,
    @Json(name = "deadline")
    val deadline:String,
    @Json(name = "tech")
    val tech:String
)