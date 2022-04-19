package com.herokuapp.serverbugit.bugit.ui.projects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.herokuapp.serverbugit.bugit.data.ProjectRepo

class ProjectViewModelFactory(private val projectRepo:ProjectRepo):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProjectViewModel(projectRepo) as T
    }
}