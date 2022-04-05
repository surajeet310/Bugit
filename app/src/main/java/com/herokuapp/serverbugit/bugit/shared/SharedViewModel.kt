package com.herokuapp.serverbugit.bugit.shared

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel:ViewModel() {
    var userId = MutableLiveData<String>()
}