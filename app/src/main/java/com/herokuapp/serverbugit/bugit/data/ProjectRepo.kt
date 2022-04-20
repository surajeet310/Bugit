package com.herokuapp.serverbugit.bugit.data

import androidx.lifecycle.MutableLiveData
import com.herokuapp.serverbugit.api.BugitClient
import com.herokuapp.serverbugit.api.models.projects.*
import com.herokuapp.serverbugit.api.models.workspaces.WorkspaceMembers
import java.util.*

class ProjectRepo(private val token:String) {
    private val authApi = BugitClient.getAuthApiInstance()

    private var addProjectStatus = MutableLiveData<Boolean>()
    private var fetchSingleProjectData = MutableLiveData<SingleProjectResponseDataType>()
    private var fetchSingleProjectStatus = MutableLiveData<Boolean>()
    private var fetchAllProjectMembersData = MutableLiveData<List<ProjectMembers>>()
    private var fetchAllProjectMembersStatus = MutableLiveData<Boolean>()
    private var fetchProjectMembersToAddData = MutableLiveData<List<WorkspaceMembers>>()
    private var fetchProjectMembersToAddStatus = MutableLiveData<Boolean>()
    private var deleteProjectStatus = MutableLiveData<Boolean>()
    private var addProjectMemberStatus = MutableLiveData<Boolean>()
    private var makeProjectMemberAdminStatus = MutableLiveData<Boolean>()
    private var removeProjectMemberStatus = MutableLiveData<Boolean>()

    fun getAddProjectStatus() = addProjectStatus

    fun getFetchProjectStatus() = fetchSingleProjectStatus
    fun getFetchProjectData() = fetchSingleProjectData

    fun getFetchAllProjectMembersData() = fetchAllProjectMembersData
    fun getFetchAllProjectMembersStatus() = fetchAllProjectMembersStatus

    fun getFetchProjectMembersToAddData() = fetchProjectMembersToAddData
    fun getFetchProjectMembersToAddStatus() = fetchProjectMembersToAddStatus

    fun getDeleteProjectStatus() = deleteProjectStatus

    fun getAddProjectMemberStatus() = addProjectMemberStatus

    fun getMakeProjectMemberAdminStatus() = makeProjectMemberAdminStatus

    fun getRemoveProjectMemberStatus() = removeProjectMemberStatus

    suspend fun addProject(project:AddProject){
        BugitClient.authToken = token
        val addProjectResponse = authApi.addProject(project)
        if (addProjectResponse.body() != null){
            addProjectStatus.postValue(true)
        }
        else{
            addProjectStatus.postValue(false)
        }
    }

    suspend fun fetchSingleProject(projectId:UUID,userId:UUID){
        BugitClient.authToken = token
        val fetchSingleProjectResponse = authApi.getSingleProject(projectId, userId)
        if (fetchSingleProjectResponse.body() != null){
            fetchSingleProjectData.postValue(fetchSingleProjectResponse.body()!!.result!!)
            fetchSingleProjectStatus.postValue(true)
        }
        else{
            fetchSingleProjectStatus.postValue(false)
        }
    }

    suspend fun fetchAllProjectMembers(projectId:UUID){
        BugitClient.authToken = token
        val fetchAllProjectMembersResponse = authApi.getAllProjectMembers(projectId)
        if (fetchAllProjectMembersResponse.body() != null){
            fetchAllProjectMembersData.postValue(fetchAllProjectMembersResponse.body()!!.result!!)
            fetchAllProjectMembersStatus.postValue(true)
        }
        else{
            fetchAllProjectMembersStatus.postValue(false)
        }
    }

    suspend fun fetchProjectMembersToAdd(workspaceId:UUID,projectId: UUID){
        BugitClient.authToken = token
        val fetchProjectMembersToAddResponse = authApi.getWorkspaceMembers(workspaceId, projectId)
        if (fetchProjectMembersToAddResponse.body() != null){
            fetchProjectMembersToAddData.postValue(fetchProjectMembersToAddResponse.body()!!.result!!)
            fetchProjectMembersToAddStatus.postValue(true)
        }
        else{
            fetchProjectMembersToAddStatus.postValue(false)
        }
    }

    suspend fun deleteProject(projectId: UUID){
        BugitClient.authToken = token
        val deleteProjectResponse = authApi.deleteProject(projectId)
        if (deleteProjectResponse.body() != null){
            deleteProjectStatus.postValue(true)
        }
        else{
            deleteProjectStatus.postValue(true)
        }
    }

    suspend fun addProjectMember(member:AddProjectMember){
        BugitClient.authToken = token
        val addProjectMemberResponse = authApi.addProjectMember(member)
        if (addProjectMemberResponse.body() != null){
            addProjectMemberStatus.postValue(true)
        }
        else{
            addProjectMemberStatus.postValue(false)
        }
    }

    suspend fun makeProjectMemberAdmin(member:MakeProjectMemberAdmin){
        BugitClient.authToken = token
        val makeProjectMemberAdminResponse = authApi.makeProjectUserAdmin(member)
        if (makeProjectMemberAdminResponse.body() != null){
            makeProjectMemberAdminStatus.postValue(true)
        }
        else{
            makeProjectMemberAdminStatus.postValue(false)
        }
    }

    suspend fun removeProjectMember(projectId: UUID,userId: UUID){
        BugitClient.authToken = token
        val removeProjectMemberResponse = authApi.removeProjectMember(projectId, userId)
        if (removeProjectMemberResponse.body() != null){
            removeProjectMemberStatus.postValue(true)
        }
        else{
            removeProjectMemberStatus.postValue(true)
        }
    }
}