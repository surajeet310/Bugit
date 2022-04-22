package com.herokuapp.serverbugit.bugit.data

import androidx.lifecycle.MutableLiveData
import com.herokuapp.serverbugit.api.BugitClient
import com.herokuapp.serverbugit.api.models.projects.ProjectMembers
import com.herokuapp.serverbugit.api.models.tasks.AddComment
import com.herokuapp.serverbugit.api.models.tasks.AddTask
import com.herokuapp.serverbugit.api.models.tasks.AssignTask
import com.herokuapp.serverbugit.api.models.tasks.SingleTaskResponseDataType
import java.util.*

class TaskRepo(private val token:String) {
    private val authApi = BugitClient.getAuthApiInstance()

    private var addTaskStatus = MutableLiveData<Boolean>()
    private var fetchSingleTaskData = MutableLiveData<SingleTaskResponseDataType>()
    private var fetchSingleTaskStatus = MutableLiveData<Boolean>()
    private var assignTaskStatus = MutableLiveData<Boolean>()
    private var deleteTaskStatus = MutableLiveData<Boolean>()
    private var postCommentStatus = MutableLiveData<Boolean>()
    private var fetchTaskMembersToAddStatus = MutableLiveData<Boolean>()
    private var fetchTaskMembersToAddData = MutableLiveData<List<ProjectMembers>>()

    fun getAddTaskStatus() = addTaskStatus

    fun getFetchSingleTaskData() = fetchSingleTaskData
    fun getFetchSingleTaskStatus() = fetchSingleTaskStatus

    fun getAssignTaskStatus() = assignTaskStatus

    fun getDeleteTaskStatus() = deleteTaskStatus

    fun getPostCommentStatus() = postCommentStatus

    fun getFetchTaskMembersToAddStatus() = fetchTaskMembersToAddStatus
    fun getFetchTaskMembersToAddData() = fetchTaskMembersToAddData

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

    suspend fun fetchSingleTask(taskId:UUID){
        BugitClient.authToken = token
        val fetchSingleTaskResponse = authApi.getSingleTask(taskId)
        if (fetchSingleTaskResponse.body() != null){
            fetchSingleTaskData.postValue(fetchSingleTaskResponse.body()!!.result!!)
            fetchSingleTaskStatus.postValue(true)
        }
        else{
            fetchSingleTaskStatus.postValue(false)
        }
    }

    suspend fun assignTask(user:AssignTask){
        BugitClient.authToken = token
        val assignTaskResponse = authApi.assignTask(user)
        if (assignTaskResponse.body() != null){
            assignTaskStatus.postValue(true)
        }
        else{
            assignTaskStatus.postValue(false)
        }
    }

    suspend fun deleteTask(taskId:UUID){
        BugitClient.authToken = token
        val deleteTaskResponse = authApi.deleteTask(taskId)
        if (deleteTaskResponse.body() != null){
            deleteTaskStatus.postValue(true)
        }
        else{
            deleteTaskStatus.postValue(false)
        }
    }

    suspend fun postComment(comment:AddComment){
        BugitClient.authToken = token
        val postCommentResponse = authApi.addComment(comment)
        if (postCommentResponse.body() != null){
            postCommentStatus.postValue(true)
        }
        else{
            postCommentStatus.postValue(false)
        }
    }

    suspend fun fetchTaskMembersToAdd(projectId:UUID,taskId:UUID){
        BugitClient.authToken = token
        val fetchTaskMembersToAddResponse = authApi.getProjectMembers(projectId, taskId)
        if (fetchTaskMembersToAddResponse.body() != null){
            fetchTaskMembersToAddData.postValue(fetchTaskMembersToAddResponse.body()!!.result!!)
            fetchTaskMembersToAddStatus.postValue(true)
        }
        else{
            fetchTaskMembersToAddStatus.postValue(false)
        }
    }
}