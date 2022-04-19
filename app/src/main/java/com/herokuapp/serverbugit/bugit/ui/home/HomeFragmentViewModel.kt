package com.herokuapp.serverbugit.bugit.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herokuapp.serverbugit.api.models.workspaces.AddWorkspace
import com.herokuapp.serverbugit.api.models.workspaces.AddWorkspaceMemberRequest
import com.herokuapp.serverbugit.bugit.data.HomeRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class HomeFragmentViewModel(private val homeRepo: HomeRepo):ViewModel() {

    var homeResponseData = homeRepo.getHomeResponseData()
    var homeResponseStatus = homeRepo.getHomeResponseStatus()

    var addWorkspaceResponseStatus = homeRepo.getAddWorkspaceStatus()

    var deleteWorkspaceStatus = homeRepo.getDeleteWorkspaceStatus()

    var singleWorkspaceResponseData = homeRepo.getSingleWorkspaceData()
    var singleWorkspaceResponseStatus = homeRepo.getSingleWorkspaceResponseStatus()

    var allWorkspaceMembersStatus = homeRepo.getAllWorkspaceMembersResponseStatus()
    var allWorkspaceMembersData = homeRepo.getAllWorkspaceMembersData()

    var deleteWorkspaceMemberStatus = homeRepo.getDeleteWorkspaceMemberResponseStatus()

    var makeUserAdminStatus = homeRepo.getMakeUserAdminResponseStatus()

    var addWorkspaceMemberReqStatus = homeRepo.getAddWorkspaceMemberReqStatus()

    fun getHome(){
        viewModelScope.launch(Dispatchers.IO) {
            homeRepo.fetchHome()
        }
    }

    fun addWorkspace(userId:String,name:String,description:String,createdAt:String){
        val workspace = AddWorkspace(UUID.fromString(userId),name,description,createdAt)
        viewModelScope.launch(Dispatchers.IO) {
            homeRepo.addWorkspace(workspace)
        }
    }

    fun deleteWorkspace(workspaceId:UUID){
        viewModelScope.launch(Dispatchers.IO) {
            homeRepo.deleteWorkspace(workspaceId)
        }
    }

    fun getSingleWorkspace(workspaceId: UUID){
        viewModelScope.launch(Dispatchers.IO) {
            homeRepo.fetchSingleWorkspace(workspaceId)
        }
    }

    fun getAllWorkspaceMembers(workspaceId: UUID){
        viewModelScope.launch(Dispatchers.IO) {
            homeRepo.fetchAllWorkspaceMembers(workspaceId)
        }
    }

    fun removeWorkspaceMember(workspaceId: UUID){
        viewModelScope.launch(Dispatchers.IO) {
            homeRepo.deleteWorkspaceMember(workspaceId)
        }
    }

    fun makeWorkspaceUserAdmin(workspaceId: UUID){
        viewModelScope.launch(Dispatchers.IO) {
            homeRepo.makeUserAdmin(workspaceId)
        }
    }

    fun makeWorkspaceMemberRequest(workspaceId: UUID,userId:UUID,email:String){
        val member = AddWorkspaceMemberRequest(workspaceId,email,userId)
        viewModelScope.launch(Dispatchers.IO) {
            homeRepo.addWorkspaceMemberRequest(member)
        }
    }

}