package com.herokuapp.serverbugit.bugit.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herokuapp.serverbugit.api.models.users.UserRegister
import com.herokuapp.serverbugit.bugit.data.SignUpRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpViewModel(private val signUpRepo: SignUpRepo):ViewModel() {
    var name = MutableLiveData<String>()
    var email = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    var confirmPassword = MutableLiveData<String>()

    var signUpResponse:LiveData<String> = signUpRepo.getResponseMsg()

    fun registerUser(fname:String,lname:String,email:String,password:String){
        val user = UserRegister(fname, lname, email, password)
        viewModelScope.launch(Dispatchers.IO) {
            signUpRepo.signUpUser(user)
        }
    }
}