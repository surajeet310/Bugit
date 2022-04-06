package com.herokuapp.serverbugit.bugit.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herokuapp.serverbugit.api.models.workspaces.AddWorkspace
import com.herokuapp.serverbugit.bugit.data.HomeRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class HomeFragmentViewModel(private val homeRepo: HomeRepo):ViewModel() {

    var homeResponseData = homeRepo.getHomeResponseData()
    var homeResponseStatus = homeRepo.getHomeResponseStatus()

    var addWorkspaceResponseStatus = homeRepo.getAddWorkspaceStatus()

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

}