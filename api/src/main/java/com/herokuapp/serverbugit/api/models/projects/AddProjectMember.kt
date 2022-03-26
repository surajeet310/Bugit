package com.herokuapp.serverbugit.api.models.projects

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class AddProjectMember(
    @Json(name = "p_id")
    val projectId:UUID,
    @Json(name = "user_id")
    val userId:UUID
)
