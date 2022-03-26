package com.herokuapp.serverbugit.api.models.workspaces

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class AddWorkspace(
    @Json(name = "user_id")
    val userId: UUID,
    @Json(name = "name")
    val name:String,
    @Json(name = "descp")
    val desc:String,
    @Json(name = "created_at")
    val createdAt:String
)
