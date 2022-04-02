package com.herokuapp.serverbugit.bugit.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.herokuapp.serverbugit.bugit.data.SignUpRepo

class SignUpViewModelFactory(private val signUpRepo: SignUpRepo): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SignUpViewModel(signUpRepo) as T
    }
}