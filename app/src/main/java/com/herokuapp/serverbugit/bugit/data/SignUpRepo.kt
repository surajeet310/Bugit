package com.herokuapp.serverbugit.bugit.data

import androidx.lifecycle.MutableLiveData
import com.herokuapp.serverbugit.api.BugitClient
import com.herokuapp.serverbugit.api.models.users.UserRegister

class SignUpRepo {
    private val publicApiHandler = BugitClient.getPublicApiInstance()

    private val responseMsg = MutableLiveData<String>()
    fun getResponseMsg() = responseMsg

    suspend fun signUpUser(user:UserRegister){
        val signUpResponse = publicApiHandler.registerUser(user)
        if (signUpResponse.body() != null){
            responseMsg.postValue(signUpResponse.body()!!.response)
        }
        else{
            responseMsg.postValue("error")
        }
    }
}