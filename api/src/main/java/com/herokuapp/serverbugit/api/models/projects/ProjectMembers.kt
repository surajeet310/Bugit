package com.herokuapp.serverbugit.api.models.projects

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class ProjectMembers(
    @Json(name = "user_id")
    val userId:UUID,
    @Json(name = "is_admin")
    val isAdmin:Boolean,
    @Json(name = "is_assigned")
    val isAssigned:Boolean,
    @Json(name = "user_name")
    val username:String
)
