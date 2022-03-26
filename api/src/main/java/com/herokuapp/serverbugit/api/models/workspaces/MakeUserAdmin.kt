package com.herokuapp.serverbugit.api.models.workspaces

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class MakeUserAdmin(
    @Json(name = "w_id")
    val workspaceId:UUID,
    @Json(name = "user_id")
    val userId:UUID
)
