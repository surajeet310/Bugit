package com.herokuapp.serverbugit.api.models.workspaces

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class Request(
    @Json(name = "req_id")
    val reqId:UUID,
    @Json(name = "w_id")
    val workspaceId:UUID,
    @Json(name = "user_id")
    val userId:UUID,
    @Json(name = "requestee")
    val requestee:String,
    @Json(name = "priority")
    val priority:Int
)
