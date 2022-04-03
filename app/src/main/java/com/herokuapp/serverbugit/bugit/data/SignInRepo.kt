package com.herokuapp.serverbugit.bugit.data

import androidx.lifecycle.MutableLiveData
import com.herokuapp.serverbugit.api.BugitClient
import com.herokuapp.serverbugit.api.models.users.UserSignIn

class SignInRepo {
    private val publicApi = BugitClient.getPublicApiInstance()

    private var token = MutableLiveData<String?>()
    fun getToken() = token

    private var signInResponseStatus = MutableLiveData<String>()
    fun getSignInResponseStatus() = signInResponseStatus

    suspend fun signInUser(user:UserSignIn){
        val signInResponse = publicApi.signInUser(user)
        if (signInResponse.body() != null){
            signInResponseStatus.postValue(signInResponse.body()!!.response)
            token.postValue(signInResponse.body()!!.result as String?)
        }
        else{
            signInResponseStatus.postValue("error")
            token.postValue(null)
        }
    }
}