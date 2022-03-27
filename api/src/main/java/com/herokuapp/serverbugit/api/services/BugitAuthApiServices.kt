package com.herokuapp.serverbugit.api.services

import com.herokuapp.serverbugit.api.models.GlobalServerResponse
import com.herokuapp.serverbugit.api.models.projects.*
import com.herokuapp.serverbugit.api.models.tasks.AddComment
import com.herokuapp.serverbugit.api.models.tasks.AddTask
import com.herokuapp.serverbugit.api.models.tasks.AssignTask
import com.herokuapp.serverbugit.api.models.tasks.SingleTaskResponse
import com.herokuapp.serverbugit.api.models.users.*
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


    //TODO Project Endpoints

    @GET("/auth/project")
    suspend fun getSingleProject(
        @Query("project_id") projectId:UUID
    ):Response<SingleProjectResponse>

    @GET("/auth/projectMembers")
    suspend fun getProjectMembers(
        @Query("project_id") projectId:UUID,@Query("task_id") taskId:UUID
    ):Response<ProjectMemberResponse>

    @GET("/auth/allProjectMembers")
    suspend fun getAllProjectMembers(
        @Query("project_id") projectId:UUID
    ):Response<ProjectMemberResponse>

    @POST("/auth/addProject")
    suspend fun addProject(
        @Body project:AddProject
    ):Response<GlobalServerResponse>

    @POST("/auth/addProjectMember")
    suspend fun addProjectMember(
        @Body member:AddProjectMember
    ):Response<GlobalServerResponse>

    @POST("/auth/makeProjectUserAdmin")
    suspend fun makeProjectUserAdmin(
        @Body member:MakeProjectMemberAdmin
    ):Response<GlobalServerResponse>

    @DELETE("/auth/deleteProject")
    suspend fun deleteProject(
        @Query("project_id") projectId: UUID
    ):Response<GlobalServerResponse>

    @DELETE("/auth/removeProjectMember")
    suspend fun removeProjectMember(
        @Query("project_id") projectId: UUID, @Query("user_id") userId: UUID
    ):Response<GlobalServerResponse>

    //TODO Task Endpoints

    @GET("/auth/task")
    suspend fun getSingleTask(
        @Query("task_id") taskId:UUID
    ):Response<SingleTaskResponse>

    @POST("/auth/addTask")
    suspend fun addTask(
        @Body task:AddTask
    ):Response<GlobalServerResponse>

    @POST("/auth/addComment")
    suspend fun addComment(
        @Body comment:AddComment
    ):Response<GlobalServerResponse>

    @POST("/auth/assignTask")
    suspend fun assignTask(
        @Body user:AssignTask
    ):Response<GlobalServerResponse>

    @DELETE("/auth/deleteTask")
    suspend fun deleteTask(
        @Query("task_id") taskId: UUID
    ):Response<GlobalServerResponse>

    //TODO User Endpoints

    @GET("/auth/user")
    suspend fun getUser(
        @Query("user_id") userId: UUID
    ):Response<UserResponse>

    @GET("/auth/getUserId")
    suspend fun getUserId():Response<GlobalServerResponse>

    @POST("/auth/checkPwd")
    suspend fun checkPassword(
        @Body password:UserCheckPassword
    ):Response<GlobalServerResponse>

    @PATCH("/auth/changeFname")
    suspend fun changeFname(
        @Body fname:UserUpdateFname
    ):Response<GlobalServerResponse>

    @PATCH("/auth/changeLname")
    suspend fun changeLname(
        @Body lname:UserUpdateLname
    ):Response<GlobalServerResponse>

    @PATCH("/auth/changePwd")
    suspend fun changePwd(
        @Body password:UserUpdatePassword
    ):Response<GlobalServerResponse>

    @DELETE("/auth/deleteUser")
    suspend fun deleteUser(
        @Query("user_id") userId: UUID
    ):Response<GlobalServerResponse>
}