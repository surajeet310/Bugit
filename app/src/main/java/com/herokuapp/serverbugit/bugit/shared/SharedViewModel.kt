package com.herokuapp.serverbugit.bugit.shared

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class SharedViewModel:ViewModel() {
    var userId = MutableLiveData<String>()
    var token = MutableLiveData<String>()
    var workspaceId = MutableLiveData<UUID>()
    var projectId = MutableLiveData<UUID>()
    var taskId = MutableLiveData<UUID>()
    var workspace_admin = MutableLiveData<Boolean>()
    var project_admin = MutableLiveData<Boolean>()
}