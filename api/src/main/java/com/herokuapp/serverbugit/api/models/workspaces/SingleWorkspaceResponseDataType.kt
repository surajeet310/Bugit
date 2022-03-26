package com.herokuapp.serverbugit.api.models.workspaces

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SingleWorkspaceResponseDataType(
    @Json(name = "workspace")
    val singleWorkspace: SingleWorkspace,
    @Json(name = "projects")
    val projects:List<Project>?
)
