package com.herokuapp.serverbugit.bugit.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.herokuapp.serverbugit.bugit.data.TaskRepo

class TaskViewModelFactory(private val taskRepo:TaskRepo):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TaskViewModel(taskRepo) as T
    }
}