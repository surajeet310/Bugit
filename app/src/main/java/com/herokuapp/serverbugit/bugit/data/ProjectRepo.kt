package com.herokuapp.serverbugit.bugit.data

import androidx.lifecycle.MutableLiveData
import com.herokuapp.serverbugit.api.BugitClient
import com.herokuapp.serverbugit.api.models.projects.AddProject

class ProjectRepo(private val token:String) {
    private val authApi = BugitClient.getAuthApiInstance()

    private var addProjectStatus = MutableLiveData<Boolean>()
    fun getAddProjectStatus() = addProjectStatus

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
}