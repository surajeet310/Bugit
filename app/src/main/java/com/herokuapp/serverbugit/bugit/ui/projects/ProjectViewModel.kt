package com.herokuapp.serverbugit.bugit.ui.projects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herokuapp.serverbugit.api.models.projects.AddProject
import com.herokuapp.serverbugit.api.models.projects.AddProjectMember
import com.herokuapp.serverbugit.api.models.projects.MakeProjectMemberAdmin
import com.herokuapp.serverbugit.bugit.data.ProjectRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class ProjectViewModel(private val projectRepo: ProjectRepo):ViewModel() {

    var addProjectResponseStatus = projectRepo.getAddProjectStatus()

    var singleProjectStatus = projectRepo.getFetchProjectStatus()
    var singleProjectData = projectRepo.getFetchProjectData()

    var allProjectMembersStatus = projectRepo.getFetchAllProjectMembersStatus()
    var allProjectMembersData = projectRepo.getFetchAllProjectMembersData()

    var projectMembersToAddStatus = projectRepo.getFetchProjectMembersToAddStatus()
    var projectMembersToAddData = projectRepo.getFetchProjectMembersToAddData()

    var deleteProjectStatus = projectRepo.getDeleteProjectStatus()

    var addProjectMemberStatus = projectRepo.getAddProjectMemberStatus()

    var makeProjectMemberAdminStatus = projectRepo.getMakeProjectMemberAdminStatus()

    var removeProjectMemberStatus = projectRepo.getRemoveProjectMemberStatus()

    fun addProject(workspaceId:UUID,userId:UUID,name:String,desc:String,createdAt:String,tech:String,deadline:String){
        val project = AddProject(workspaceId,userId,name, desc, createdAt, tech, deadline)
        viewModelScope.launch(Dispatchers.IO) {
            projectRepo.addProject(project)
        }
    }

    fun getSingleProject(projectId:UUID,userId:UUID){
        viewModelScope.launch(Dispatchers.IO) {
            projectRepo.fetchSingleProject(projectId, userId)
        }
    }

    fun getAllProjectMembers(projectId:UUID){
        viewModelScope.launch(Dispatchers.IO) {
            projectRepo.fetchAllProjectMembers(projectId)
        }
    }

    fun getProjectMembersToAdd(workspaceId:UUID,projectId: UUID){
        viewModelScope.launch(Dispatchers.IO) {
            projectRepo.fetchProjectMembersToAdd(workspaceId, projectId)
        }
    }

    fun deleteProject(projectId: UUID){
        viewModelScope.launch(Dispatchers.IO) {
            projectRepo.deleteProject(projectId)
        }
    }

    fun addProjectMember(projectId: UUID,userId: UUID){
        val member = AddProjectMember(projectId, userId)
        viewModelScope.launch(Dispatchers.IO) {
            projectRepo.addProjectMember(member)
        }
    }

    fun makeProjectMemberAdmin(projectId: UUID,userId: UUID){
        val member = MakeProjectMemberAdmin(projectId, userId)
        viewModelScope.launch(Dispatchers.IO) {
            projectRepo.makeProjectMemberAdmin(member)
        }
    }

    fun removeProjectMember(projectId: UUID,userId: UUID){
        viewModelScope.launch(Dispatchers.IO) {
            projectRepo.removeProjectMember(projectId, userId)
        }
    }
}