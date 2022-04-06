package com.herokuapp.serverbugit.bugit.data

import androidx.lifecycle.MutableLiveData
import com.herokuapp.serverbugit.api.BugitClient
import com.herokuapp.serverbugit.api.models.workspaces.AddWorkspace
import com.herokuapp.serverbugit.api.models.workspaces.Home
import java.util.*

class HomeRepo(private val token:String,private val userId:String) {
    private val authApi = BugitClient.getAuthApiInstance()
    private var homeResponseData = MutableLiveData<List<Home>?>()
    private var homeResponseStatus = MutableLiveData<Boolean>()
    private var addWorkspaceStatus = MutableLiveData<Boolean>()

    fun getHomeResponseData() = homeResponseData
    fun getHomeResponseStatus() = homeResponseStatus

    fun getAddWorkspaceStatus() = addWorkspaceStatus

    suspend fun fetchHome(){
        BugitClient.authToken = token
        val homeResponse = authApi.getWorkspaces(UUID.fromString(userId))
        if (homeResponse.body() != null){
            homeResponseStatus.postValue(true)
            homeResponseData.postValue(homeResponse.body()!!.result)
        }
        else{
            homeResponseStatus.postValue(false)
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
}