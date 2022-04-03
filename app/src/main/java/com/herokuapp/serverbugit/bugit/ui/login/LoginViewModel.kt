package com.herokuapp.serverbugit.bugit.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herokuapp.serverbugit.api.models.users.UserSignIn
import com.herokuapp.serverbugit.bugit.data.SignInRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepo: SignInRepo):ViewModel() {
    var email = MutableLiveData<String>()
    var password = MutableLiveData<String>()

    var token:LiveData<String?> = loginRepo.getToken()
    var signInResponseStatus:LiveData<String> = loginRepo.getSignInResponseStatus()

    fun signInUser(email:String,password:String){
        val user = UserSignIn(email, password)
        viewModelScope.launch(Dispatchers.IO) {
            loginRepo.signInUser(user)
        }
    }
}