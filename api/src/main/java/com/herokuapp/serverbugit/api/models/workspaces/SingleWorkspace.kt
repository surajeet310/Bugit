package com.herokuapp.serverbugit.api.models.workspaces

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class SingleWorkspace(
    @Json(name = "w_id")
    val workspaceId:UUID,
    @Json(name = "name")
    val name:String,
    @Json(name = "descp")
    val desc:String,
    @Json(name = "project_count")
    val projectCount:Int,
    @Json(name = "member_count")
    val memberCount:Int,
    @Json(name = "created_at")
    val createdAt:String
)