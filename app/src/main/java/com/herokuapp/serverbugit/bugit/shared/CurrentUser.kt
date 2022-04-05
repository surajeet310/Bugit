package com.herokuapp.serverbugit.bugit.shared

import androidx.lifecycle.MutableLiveData
import com.herokuapp.serverbugit.api.BugitClient
import com.herokuapp.serverbugit.api.models.users.UserDetails
import java.util.*

class CurrentUser(private val token: String) {

    private val userId = MutableLiveData<String>()
    private val user = MutableLiveData<UserDetails>()
    private val authApi = BugitClient.getAuthApiInstance()

    fun getUserId() = userId
    fun getUserDetail() = user

    suspend fun getUser(){
        BugitClient.authToken = token
        val response = authApi.getUserId()
        if (response.body() != null){
            userId.postValue(response.body()!!.result as String)
        }
    }

    suspend fun getUserDetails(userId:String){
        BugitClient.authToken = token
        val response = authApi.getUser(UUID.fromString(userId))
        if (response.body() != null){
            user.postValue(response.body()!!.result!!)
        }
    }
}