package com.herokuapp.serverbugit.bugit.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herokuapp.serverbugit.api.models.tasks.AddTask
import com.herokuapp.serverbugit.bugit.data.TaskRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class TaskViewModel(private val taskRepo: TaskRepo):ViewModel() {

    var addTaskStatus = taskRepo.getAddTaskStatus()

    fun addTask(projectId:UUID,name:String,desc:String,assignee:UUID,createdAt:String,deadline:String,tech:String){
        val task = AddTask(projectId, name, desc, assignee, createdAt, deadline, tech)
        viewModelScope.launch(Dispatchers.IO) {
            taskRepo.addTask(task)
        }
    }
}