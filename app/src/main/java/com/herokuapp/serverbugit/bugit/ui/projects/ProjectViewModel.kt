package com.herokuapp.serverbugit.bugit.ui.projects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herokuapp.serverbugit.api.models.projects.AddProject
import com.herokuapp.serverbugit.bugit.data.ProjectRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class ProjectViewModel(private val projectRepo: ProjectRepo):ViewModel() {

    var addProjectResponseStatus = projectRepo.getAddProjectStatus()

    fun addProject(workspaceId:UUID,userId:UUID,name:String,desc:String,createdAt:String,tech:String,deadline:String){
        val project = AddProject(workspaceId,userId,name, desc, createdAt, tech, deadline)
        viewModelScope.launch(Dispatchers.IO) {
            projectRepo.addProject(project)
        }
    }
}