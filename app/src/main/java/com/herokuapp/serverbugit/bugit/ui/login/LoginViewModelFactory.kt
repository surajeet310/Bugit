package com.herokuapp.serverbugit.bugit.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.herokuapp.serverbugit.bugit.data.SignInRepo

class LoginViewModelFactory(private val loginRepo: SignInRepo):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(loginRepo) as T
    }
}