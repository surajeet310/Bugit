package com.herokuapp.serverbugit.bugit.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herokuapp.serverbugit.bugit.data.HomeRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragmentViewModel(private val homeRepo: HomeRepo):ViewModel() {

    var homeResponseData = homeRepo.getHomeResponseData()
    var homeResponseStatus = homeRepo.getHomeResponseStatus()

    fun getHome(){
        viewModelScope.launch(Dispatchers.IO) {
            homeRepo.fetchHome()
        }
    }

}