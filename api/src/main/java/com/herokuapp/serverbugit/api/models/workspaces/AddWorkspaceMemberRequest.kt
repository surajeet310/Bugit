package com.herokuapp.serverbugit.api.models.workspaces

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class AddWorkspaceMemberRequest(
    @Json(name = "w_id")
    val workspaceId:UUID,
    @Json(name = "email")
    val email:String,
    @Json(name = "requestee_id")
    val requesteeId:UUID
)