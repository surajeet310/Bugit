package com.herokuapp.serverbugit.api.models.workspaces

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class WorkspaceMembers(
    @Json(name = "user_id")
    val userId:UUID,
    @Json(name = "is_admin")
    val isAdmin:Boolean,
    @Json(name = "is_taken")
    val isTaken:Boolean,
    @Json(name = "user_name")
    val username:String
)
