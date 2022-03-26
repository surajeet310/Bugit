package com.herokuapp.serverbugit.api.models.users

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class UserUpdateFname(
    @Json(name = "user_id")
    val userId:UUID,
    @Json(name = "fname")
    val fname:String
)
