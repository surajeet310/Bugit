package com.herokuapp.serverbugit.api.models.projects

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class AddProject(
    @Json(name = "w_id")
    val workspaceId:UUID,
    @Json(name = "user_id")
    val userId:UUID,
    @Json(name = "name")
    val name:String,
    @Json(name = "descp")
    val desc:String,
    @Json(name = "created_at")
    val createdAt:String,
    @Json(name = "tech")
    val tech:String,
    @Json(name = "deadline")
    val deadline:String
)