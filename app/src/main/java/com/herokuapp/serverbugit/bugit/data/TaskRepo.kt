package com.herokuapp.serverbugit.bugit.data

import androidx.lifecycle.MutableLiveData
import com.herokuapp.serverbugit.api.BugitClient
import com.herokuapp.serverbugit.api.models.tasks.AddTask

class TaskRepo(private val token:String) {
    private val authApi = BugitClient.getAuthApiInstance()

    private var addTaskStatus = MutableLiveData<Boolean>()

    fun getAddTaskStatus() = addTaskStatus

    suspend fun addTask(task:AddTask){
        BugitClient.authToken = token
        val addTaskResponse = authApi.addTask(task)
        if (addTaskResponse.body() != null){
            addTaskStatus.postValue(true)
        }
        else{
            addTaskStatus.postValue(false)
        }
    }
}