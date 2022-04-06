package com.herokuapp.serverbugit.bugit.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.herokuapp.serverbugit.bugit.data.HomeRepo

class HomeFragmentViewModelFactory(private val homeRepo: HomeRepo):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeFragmentViewModel(homeRepo) as T
    }
}