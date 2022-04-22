package com.herokuapp.serverbugit.bugit.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herokuapp.serverbugit.api.models.tasks.AddComment
import com.herokuapp.serverbugit.api.models.tasks.AddTask
import com.herokuapp.serverbugit.api.models.tasks.AssignTask
import com.herokuapp.serverbugit.bugit.data.TaskRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class TaskViewModel(private val taskRepo: TaskRepo):ViewModel() {

    var addTaskStatus = taskRepo.getAddTaskStatus()

    var singleTaskStatus = taskRepo.getFetchSingleTaskStatus()
    var singleTaskData = taskRepo.getFetchSingleTaskData()

    var assignTaskStatus = taskRepo.getAssignTaskStatus()

    var deleteTaskStatus = taskRepo.getDeleteTaskStatus()

    var postCommentStatus = taskRepo.getPostCommentStatus()

    var taskMembersToAddData = taskRepo.getFetchTaskMembersToAddData()
    var taskMembersToAddStatus = taskRepo.getFetchTaskMembersToAddStatus()

    fun addTask(projectId:UUID,name:String,desc:String,assignee:UUID,createdAt:String,deadline:String,tech:String){
        val task = AddTask(projectId, name, desc, assignee, createdAt, deadline, tech)
        viewModelScope.launch(Dispatchers.IO) {
            taskRepo.addTask(task)
        }
    }

    fun getSingleTask(taskId:UUID){
        viewModelScope.launch(Dispatchers.IO) {
            taskRepo.fetchSingleTask(taskId)
        }
    }

    fun assignTask(taskId:UUID,userId:UUID){
        val user = AssignTask(taskId, userId)
        viewModelScope.launch(Dispatchers.IO) {
            taskRepo.assignTask(user)
        }
    }

    fun deleteTask(taskId:UUID){
        viewModelScope.launch(Dispatchers.IO) {
            taskRepo.deleteTask(taskId)
        }
    }

    fun postComment(taskId:UUID,userId:UUID,comment:String,createdAt: String){
        val comment = AddComment(taskId,userId,comment,createdAt)
        viewModelScope.launch(Dispatchers.IO) {
            taskRepo.postComment(comment)
        }
    }

    fun getTaskMembersToAdd(projectId: UUID,taskId: UUID){
        viewModelScope.launch(Dispatchers.IO) {
            taskRepo.fetchTaskMembersToAdd(projectId, taskId)
        }
    }
}