package com.herokuapp.serverbugit.bugit.data

import androidx.lifecycle.MutableLiveData
import com.herokuapp.serverbugit.api.BugitClient
import com.herokuapp.serverbugit.api.models.workspaces.*
import java.util.*

class HomeRepo(private val token:String) {
    private val authApi = BugitClient.getAuthApiInstance()
    private var homeResponseData = MutableLiveData<List<Home>?>()
    private var homeResponseStatus = MutableLiveData<Boolean>()
    private var addWorkspaceStatus = MutableLiveData<Boolean>()
    private var deleteWorkspaceStatus = MutableLiveData<Boolean>()
    private var singleWorkspaceData = MutableLiveData<SingleWorkspaceResponseDataType>()
    private var singleWorkspaceResponseStatus = MutableLiveData<Boolean>()
    private var allWorkspaceMembersData = MutableLiveData<List<WorkspaceMembers>>()
    private var allWorkspaceMembersResponseStatus = MutableLiveData<Boolean>()
    private var deleteWorkspaceMemberResponseStatus = MutableLiveData<Boolean>()
    private var makeUserAdminResponseStatus = MutableLiveData<Boolean>()
    private var addWorkspaceMemberReqStatus = MutableLiveData<Boolean>()
    private var fetchRequestData = MutableLiveData<List<Request>?>()
    private var fetchRequestStatus = MutableLiveData<Boolean>()
    private var addWorkspaceMemberStatus = MutableLiveData<Boolean>()
    private var denyRequestStatus = MutableLiveData<Boolean>()

    fun getHomeResponseData() = homeResponseData
    fun getHomeResponseStatus() = homeResponseStatus

    fun getAddWorkspaceStatus() = addWorkspaceStatus

    fun getDeleteWorkspaceStatus() = deleteWorkspaceStatus

    fun getSingleWorkspaceData() = singleWorkspaceData
    fun getSingleWorkspaceResponseStatus() = singleWorkspaceResponseStatus

    fun getAllWorkspaceMembersData() = allWorkspaceMembersData
    fun getAllWorkspaceMembersResponseStatus() = allWorkspaceMembersResponseStatus

    fun getDeleteWorkspaceMemberResponseStatus() = deleteWorkspaceMemberResponseStatus

    fun getMakeUserAdminResponseStatus() = makeUserAdminResponseStatus

    fun getAddWorkspaceMemberReqStatus() = addWorkspaceMemberReqStatus

    fun getFetchRequestData() = fetchRequestData
    fun getFetchRequestStatus() = fetchRequestStatus

    fun getAddWorkspaceMemberStatus() = addWorkspaceMemberStatus

    fun getDenyRequestStatus() = denyRequestStatus

    suspend fun fetchHome(userId:UUID){
        BugitClient.authToken = token
        val homeResponse = authApi.getWorkspaces(userId)
        if (homeResponse.body() != null){
            homeResponseStatus.postValue(true)
            homeResponseData.postValue(homeResponse.body()!!.result)
        }
        else{
            homeResponseStatus.postValue(false)
        }
    }

    suspend fun fetchSingleWorkspace(workspaceId: UUID,userId:UUID){
        BugitClient.authToken = token
        val singleWorkspaceResponse = authApi.getSingleWorkspace(workspaceId, userId)
        if (singleWorkspaceResponse.body() != null){
            singleWorkspaceResponseStatus.postValue(true)
            singleWorkspaceData.postValue(singleWorkspaceResponse.body()!!.result!!)
        }
        else{
            singleWorkspaceResponseStatus.postValue(false)
        }
    }

    suspend fun addWorkspace(workspace:AddWorkspace){
        BugitClient.authToken = token
        val addWorkspaceResponse = authApi.addWorkspace(workspace)
        if (addWorkspaceResponse.body() != null){
            addWorkspaceStatus.postValue(true)
        }
        else{
            addWorkspaceStatus.postValue(false)
        }
    }

    suspend fun deleteWorkspace(workspaceId:UUID){
        BugitClient.authToken = token
        val deleteWorkspaceResponse = authApi.deleteWorkspace(workspaceId)
        if (deleteWorkspaceResponse.body() != null){
            deleteWorkspaceStatus.postValue(true)
        }
        else{
            deleteWorkspaceStatus.postValue(false)
        }
    }

    suspend fun fetchAllWorkspaceMembers(workspaceId:UUID){
        BugitClient.authToken = token
        val workspaceMembersResponse = authApi.getAllWorkspaceMembers(workspaceId)
        if (workspaceMembersResponse.body() != null){
            allWorkspaceMembersData.postValue(workspaceMembersResponse.body()!!.result!!)
            allWorkspaceMembersResponseStatus.postValue(true)
        }
        else{
            allWorkspaceMembersResponseStatus.postValue(false)
        }
    }

    suspend fun deleteWorkspaceMember(workspaceId:UUID,userId:UUID){
        BugitClient.authToken = token
        val deleteWorkspaceMemberResponse = authApi.removeWorkspaceMember(workspaceId,userId)
        if (deleteWorkspaceMemberResponse.body() != null){
            deleteWorkspaceMemberResponseStatus.postValue(true)
        }
        else{
            deleteWorkspaceMemberResponseStatus.postValue(false)
        }
    }

    suspend fun makeUserAdmin(workspaceId:UUID,userId:UUID){
        BugitClient.authToken = token
        val user = MakeUserAdmin(workspaceId,userId)
        val makeUserAdminResponse = authApi.makeUserAdmin(user)
        if (makeUserAdminResponse.body() != null){
            makeUserAdminResponseStatus.postValue(true)
        }
        else{
            makeUserAdminResponseStatus.postValue(false)
        }
    }

    suspend fun addWorkspaceMemberRequest(member:AddWorkspaceMemberRequest){
        BugitClient.authToken = token
        val addWorkspaceMemberReqResponse = authApi.addWorkspaceMemberReq(member)
        if (addWorkspaceMemberReqResponse.body() != null){
            addWorkspaceMemberReqStatus.postValue(true)
        }
        else{
            addWorkspaceMemberReqStatus.postValue(false)
        }
    }

    suspend fun fetchRequests(userId:UUID){
        BugitClient.authToken = token
        val fetchRequestResponse = authApi.getRequests(userId)
        if (fetchRequestResponse.body() != null){
            fetchRequestData.postValue(fetchRequestResponse.body()!!.result)
            fetchRequestStatus.postValue(true)
        }
        else{
            fetchRequestStatus.postValue(false)
        }
    }

    suspend fun addWorkspaceMember(member:AddWorkspaceMember){
        BugitClient.authToken = token
        val addWorkspaceMemberResponse = authApi.addWorkspaceMember(member)
        if (addWorkspaceMemberResponse.body() != null){
            addWorkspaceMemberStatus.postValue(true)
        }
        else{
            addWorkspaceMemberStatus.postValue(false)
        }
    }

    suspend fun denyRequest(request:AddWorkspaceMember){
        BugitClient.authToken = token
        val denyRequestResponse = authApi.denyWorkspaceRequest(request)
        if (denyRequestResponse.body() != null){
            denyRequestStatus.postValue(true)
        }
        else{
            denyRequestStatus.postValue(false)
        }
    }
}