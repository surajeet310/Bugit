package com.herokuapp.serverbugit.api.services

import com.herokuapp.serverbugit.api.models.GlobalServerResponse
import com.herokuapp.serverbugit.api.models.workspaces.*
import retrofit2.Response
import retrofit2.http.*
import java.util.*


interface BugitAuthApiServices {

    //TODO Workspace Endpoints

    @GET("/auth/home")
    suspend fun getWorkspaces(
        @Query("user_id") userId:UUID
    ):Response<HomeResponse>

    @GET("/auth/home/workspace")
    suspend fun getSingleWorkspace(
        @Query("workspace_id") workspaceId:UUID
    ):Response<SingleWorkspaceResponse>

    @GET("/auth/workspaceMembers")
    suspend fun getWorkspaceMembers(
        @Query("workspace_id") workspaceId: UUID, @Query("project_id") projectId:UUID
    ):Response<WorkspaceMemberResponse>

    @GET("/auth/allWorkspaceMembers")
    suspend fun getAllWorkspaceMembers(
        @Query("workspace_id") workspaceId: UUID
    ):Response<WorkspaceMemberResponse>

    @GET("/auth/requests")
    suspend fun getRequests(
        @Query("user_id") userId:UUID
    ):Response<RequestResponse>

    @POST("/auth/addWorkspace")
    suspend fun addWorkspace(
        @Body workspace:AddWorkspace
    ):Response<GlobalServerResponse>

    @POST("/auth/addWorkspaceMemberReq")
    suspend fun addWorkspaceMemberReq(
        @Body workspaceMemberRequest:AddWorkspaceMemberRequest
    ):Response<GlobalServerResponse>

    @POST("/auth/addWorkspaceMember")
    suspend fun addWorkspaceMember(
        @Body workspaceMember:AddWorkspaceMember
    ):Response<GlobalServerResponse>

    @POST("/auth/makeUserAdmin")
    suspend fun makeUserAdmin(
        @Body user:MakeUserAdmin
    ):Response<GlobalServerResponse>

    @DELETE("/auth/deleteWorkspace")
    suspend fun deleteWorkspace(
        @Query("workspace_id") workspaceId: UUID
    ):Response<GlobalServerResponse>

    @DELETE("/auth/removeWorkspaceMember")
    suspend fun removeWorkspaceMember(
        @Query("workspace_id") workspaceId: UUID, @Query("user_id") userId:UUID
    ):Response<GlobalServerResponse>
}